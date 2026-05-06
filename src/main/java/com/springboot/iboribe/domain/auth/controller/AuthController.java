package com.springboot.iboribe.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.auth.dto.request.LoginRequest;
import com.springboot.iboribe.domain.auth.dto.request.SignUpRequest;
import com.springboot.iboribe.domain.auth.dto.response.TokenResponse;
import com.springboot.iboribe.domain.auth.service.AuthService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.jwt.JwtCookieWriter;
import com.springboot.iboribe.global.security.jwt.JwtProvider;
import com.springboot.iboribe.global.security.jwt.TokenType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "사용자 인증 API")
public class AuthController {

  private final AuthService authService;
  private final JwtProvider jwtProvider;
  private final JwtCookieWriter jwtCookieWriter;

  @Operation(
      summary = "[토큰 X] 회원가입",
      description =
          """
          **Parameters**  \n
          name: 사용자 이름  \n
          username: 사용자 아이디 (4~20자, 중복 불가)  \n
          password: 사용자 비밀번호 (8~20자, 영문/숫자/특수문자 포함)  \n

          **Returns** \n
          회원가입 성공 여부 \n
          """)
  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
    authService.signUp(request);

    return ResponseEntity.status(201).body(BaseResponse.success(201, "회원가입 성공", null));
  }

  @Operation(
      summary = "[토큰 X] 로그인",
      description =
          """
          **Parameters**  \n
          username: 사용자 아이디  \n
          password: 사용자 비밀번호  \n

          **Authentication**  \n
          - **HttpOnly Cookie 기반 JWT 인증**  \n
          - 로그인 성공 시 토큰이 **쿠키에 자동 저장됨**  \n
            → ACCESS_TOKEN  \n
            → REFRESH_TOKEN  \n
          - 이후 API 요청 시 **브라우저가 쿠키를 자동 포함하여 전송**  \n

          **Additional (Bearer 지원)**  \n
          - **Authorization Header 방식도 지원**  \n
          - Authorization: Bearer {accessToken}  \n

          **Returns**  \n
          userId, name, username  \n
          accessToken, refreshToken  \n
          """)
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<TokenResponse>> login(
      @Valid @RequestBody LoginRequest request) {

    TokenResponse response = authService.login(request);

    return ResponseEntity.ok()
        .headers(createTokenHeaders(response))
        .body(BaseResponse.success(200, "로그인 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 토큰 재발급",
      description =
          """
          **Authentication**  \n
          - 쿠키의 REFRESH_TOKEN 사용  \n
          - 별도 전달 필요 없음  \n

          **Process**  \n
          - REFRESH_TOKEN 검증  \n
          - ACCESS_TOKEN 재발급  \n
          - REFRESH_TOKEN 재발급  \n
          - 쿠키에 다시 저장  \n

          **Returns**  \n
          새로운 accessToken  \n
          새로운 refreshToken  \n
          """)
  @PostMapping("/refresh")
  public ResponseEntity<BaseResponse<TokenResponse>> refresh(HttpServletRequest request) {
    String refreshToken = jwtProvider.extractRefreshToken(request);

    TokenResponse response = authService.refresh(refreshToken);

    return ResponseEntity.ok()
        .headers(createTokenHeaders(response))
        .body(BaseResponse.success(200, "토큰 재발급 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 로그아웃",
      description =
          """
          **Authentication**  \n
          - 쿠키 기반 인증 사용  \n

          **Process**  \n
          - ACCESS_TOKEN 쿠키 만료  \n
          - REFRESH_TOKEN 쿠키 만료  \n

          **Note**  \n
          - Stateless 구조 (서버 세션 없음)  \n
          - 쿠키 삭제 방식으로 로그아웃 처리  \n

          **Returns**  \n
          로그아웃 성공 여부  \n
          """)
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout() {
    authService.logout();

    HttpHeaders headers = new HttpHeaders();
    headers.add(
        HttpHeaders.SET_COOKIE,
        jwtCookieWriter.removeTokenFromCookie(TokenType.ACCESS_TOKEN).toString());
    headers.add(
        HttpHeaders.SET_COOKIE,
        jwtCookieWriter.removeTokenFromCookie(TokenType.REFRESH_TOKEN).toString());

    return ResponseEntity.ok().headers(headers).body(BaseResponse.success(200, "로그아웃 성공", null));
  }

  /** accessToken, refreshToken 쿠키 설정 */
  private HttpHeaders createTokenHeaders(TokenResponse response) {
    HttpHeaders headers = new HttpHeaders();

    headers.add(
        HttpHeaders.SET_COOKIE,
        jwtCookieWriter.addAccessTokenToCookie(response.getAccessToken()).toString());

    headers.add(
        HttpHeaders.SET_COOKIE,
        jwtCookieWriter.addRefreshTokenToCookie(response.getRefreshToken()).toString());

    return headers;
  }
}
