package com.springboot.iboribe.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.auth.dto.request.LoginRequest;
import com.springboot.iboribe.domain.auth.dto.request.RefreshRequest;
import com.springboot.iboribe.domain.auth.dto.request.SignUpRequest;
import com.springboot.iboribe.domain.auth.dto.response.DuplicateCheckResponse;
import com.springboot.iboribe.domain.auth.dto.response.TokenResponse;
import com.springboot.iboribe.domain.auth.service.AuthService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.jwt.JwtProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "사용자 인증 API")
public class AuthController {

  private final AuthService authService;
  private final JwtProvider jwtProvider;

  @SecurityRequirements
  @PostMapping("/demo-login")
  @Operation(
      summary = "[토큰 X] 데모 계정 로그인",
      description =
          """
          **Purpose**  \n
          - 공모전 심사용 데모 계정 로그인  \n
          - 별도 회원가입 없이 미리 등록된 사용자/아이/진료 기록 데이터 확인 가능  \n

          **Process**  \n
          - 서버에 등록된 mom_jiyoon 계정으로 자동 로그인  \n

          **Demo Accounts** (일반 로그인으로도 사용 가능)  \n
          가족 고유번호: YoonFamily2026!  \n
          엄마 - username: mom_jiyoon / password: Demo!1234  \n
          아빠 - username: dad_hyunwoo / password: Demo!1234  \n
          할머니 - username: grandma_soon / password: Demo!1234  \n

          **Returns**  \n
          userId, name, username  \n
          accessToken, refreshToken  \n
          """)
  public ResponseEntity<BaseResponse<TokenResponse>> demoLogin(HttpServletResponse httpResponse) {
    TokenResponse response = authService.demoLogin();

    setAccessTokenCookie(httpResponse, response.getAccessToken());

    return ResponseEntity.ok(BaseResponse.success(200, "데모 계정 로그인 성공", response));
  }

  @SecurityRequirements
  @Operation(
      summary = "[토큰 X] 회원가입",
      description =
          """
          **Parameters**  \n
          name: 사용자 이름  \n
          username: 사용자 아이디 (4~20자, 중복 불가)  \n
          password: 사용자 비밀번호 (8~20자, 영문/숫자/특수문자 포함)  \n
          familyRole: CHILD / MOTHER / FATHER / GRANDMOTHER / GRANDFATHER / SIBLING / RELATIVE  \n

          **Returns** \n
          회원가입 성공 여부 \n
          """)
  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
    authService.signUp(request);

    return ResponseEntity.status(201).body(BaseResponse.success(201, "회원가입 성공", null));
  }

  @SecurityRequirements
  @Operation(
      summary = "[토큰 X] 로그인",
      description =
          """
          **Parameters**  \n
          username: 사용자 아이디  \n
          password: 사용자 비밀번호  \n

          **Authentication**  \n
          - 로그인 성공 시 accessToken은 쿠키(HttpOnly)와 body 모두 내려줌  \n
          - 쿠키: 이후 API 요청에 자동 포함  \n
          - body accessToken: 로그인 여부/UI 상태 관리용  \n

          **Demo Accounts**  \n
          가족 고유번호: YoonFamily2026!  \n
          엄마 - username: mom_jiyoon / password: Demo!1234  \n
          아빠 - username: dad_hyunwoo / password: Demo!1234  \n
          할머니 - username: grandma_soon / password: Demo!1234  \n

          **Returns**  \n
          userId, name, username  \n
          accessToken, refreshToken  \n
          """)
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<TokenResponse>> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse httpResponse) {

    TokenResponse response = authService.login(request);

    setAccessTokenCookie(httpResponse, response.getAccessToken());

    return ResponseEntity.ok(BaseResponse.success(200, "로그인 성공", response));
  }

  @SecurityRequirements
  @Operation(
      summary = "[토큰 X] 토큰 재발급",
      description =
          """
          **Parameters**  \n
          refreshToken: 재발급에 사용할 Refresh Token  \n

          **Process**  \n
          - REFRESH_TOKEN 검증  \n
          - 새로운 ACCESS_TOKEN, REFRESH_TOKEN 발급  \n
          - 새 accessToken을 쿠키에도 세팅  \n

          **Returns**  \n
          새로운 accessToken  \n
          새로운 refreshToken  \n
          """)
  @PostMapping("/refresh")
  public ResponseEntity<BaseResponse<TokenResponse>> refresh(
      @Valid @RequestBody RefreshRequest request, HttpServletResponse httpResponse) {

    TokenResponse response = authService.refresh(request.getRefreshToken());

    setAccessTokenCookie(httpResponse, response.getAccessToken());

    return ResponseEntity.ok(BaseResponse.success(200, "토큰 재발급 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 로그아웃",
      description =
          """
          **Note**  \n
          - Stateless 구조 (서버 세션 없음)  \n
          - accessToken 쿠키 만료 처리  \n
          - 클라이언트에서도 저장된 토큰 삭제 필요  \n
          """)
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout(HttpServletResponse httpResponse) {
    authService.logout();

    ResponseCookie expiredCookie = jwtProvider.clearAccessTokenCookie();
    httpResponse.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());

    return ResponseEntity.ok(BaseResponse.success(200, "로그아웃 성공", null));
  }

  @SecurityRequirements
  @Operation(
      summary = "[토큰 X] 아이디 중복 확인",
      description =
          """
          **Parameters**  \n
          username: 중복 확인할 아이디  \n

          **Returns**  \n
          available: true = 사용 가능, false = 이미 사용 중  \n
          """,
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "중복 확인 성공",
            content =
                @Content(
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = {
                      @ExampleObject(
                          name = "사용 가능한 아이디",
                          value =
                              """
                              {
                                "success": true,
                                "code": 200,
                                "message": "아이디 중복 확인 완료",
                                "data": { "available": true }
                              }
                              """),
                      @ExampleObject(
                          name = "이미 사용 중인 아이디",
                          value =
                              """
                              {
                                "success": true,
                                "code": 200,
                                "message": "아이디 중복 확인 완료",
                                "data": { "available": false }
                              }
                              """)
                    }))
      })
  @GetMapping("/check-username")
  public ResponseEntity<BaseResponse<DuplicateCheckResponse>> checkUsername(
      @RequestParam @NotBlank String username) {

    DuplicateCheckResponse response = authService.checkUsername(username);

    return ResponseEntity.ok(BaseResponse.success(200, "아이디 중복 확인 완료", response));
  }

  @SecurityRequirements
  @Operation(
      summary = "[토큰 X] 가족고유번호 중복 확인",
      description =
          """
          **Parameters**  \n
          familyCode: 중복 확인할 가족고유번호  \n

          **Use Case**  \n
          - 회원가입 시 첫 번째 가족 구성원이 새 가족고유번호를 만들기 전 중복 확인  \n
          - 가족고유번호 수정 API에서도 활용 가능  \n

          **Returns**  \n
          available: true = 사용 가능, false = 이미 사용 중  \n
          """,
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "중복 확인 성공",
            content =
                @Content(
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = {
                      @ExampleObject(
                          name = "사용 가능한 가족고유번호",
                          value =
                              """
                              {
                                "success": true,
                                "code": 200,
                                "message": "가족고유번호 중복 확인 완료",
                                "data": { "available": true }
                              }
                              """),
                      @ExampleObject(
                          name = "이미 사용 중인 가족고유번호",
                          value =
                              """
                              {
                                "success": true,
                                "code": 200,
                                "message": "가족고유번호 중복 확인 완료",
                                "data": { "available": false }
                              }
                              """)
                    }))
      })
  @GetMapping("/check-family-code")
  public ResponseEntity<BaseResponse<DuplicateCheckResponse>> checkFamilyCode(
      @RequestParam @NotBlank String familyCode) {

    DuplicateCheckResponse response = authService.checkFamilyCode(familyCode);

    return ResponseEntity.ok(BaseResponse.success(200, "가족고유번호 중복 확인 완료", response));
  }

  private void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
    ResponseCookie cookie = jwtProvider.createAccessTokenCookie(accessToken);
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
