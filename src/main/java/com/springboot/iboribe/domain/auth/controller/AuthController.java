package com.springboot.iboribe.domain.auth.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.auth.dto.request.LoginRequest;
import com.springboot.iboribe.domain.auth.dto.request.RefreshRequest;
import com.springboot.iboribe.domain.auth.dto.request.SignUpRequest;
import com.springboot.iboribe.domain.auth.dto.response.TokenResponse;
import com.springboot.iboribe.domain.auth.service.AuthService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "사용자 인증 API")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/demo-login")
  @Operation(
      summary = "[토큰 X] 데모 계정 로그인",
      description =
          """
          **Purpose**  \n
          - 공모전 심사용 데모 계정 로그인  \n
          - 별도 회원가입 없이 미리 등록된 사용자/아이/진료 기록 데이터 확인 가능  \n

          **Process**  \n
          - 서버에 등록된 mom_jiyoon 계정으로 로그인  \n

          **Returns**  \n
          userId, name, username  \n
          accessToken, refreshToken  \n
          """)
  public ResponseEntity<BaseResponse<TokenResponse>> demoLogin() {
    TokenResponse response = authService.demoLogin();

    return ResponseEntity.ok(BaseResponse.success(200, "데모 계정 로그인 성공", response));
  }

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

  @Operation(
      summary = "[토큰 X] 로그인",
      description =
          """
          **Parameters**  \n
          username: 사용자 아이디  \n
          password: 사용자 비밀번호  \n

          **Authentication**  \n
          - 로그인 성공 시 accessToken, refreshToken 반환  \n
          - 이후 API 요청 시 Authorization: Bearer {accessToken} 헤더에 포함  \n

          **Returns**  \n
          userId, name, username  \n
          accessToken, refreshToken  \n
          """)
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<TokenResponse>> login(
      @Valid @RequestBody LoginRequest request) {

    TokenResponse response = authService.login(request);

    return ResponseEntity.ok(BaseResponse.success(200, "로그인 성공", response));
  }

  @Operation(
      summary = "[토큰 X] 토큰 재발급",
      description =
          """
          **Parameters**  \n
          refreshToken: 재발급에 사용할 Refresh Token  \n

          **Process**  \n
          - REFRESH_TOKEN 검증  \n
          - 새로운 ACCESS_TOKEN, REFRESH_TOKEN 발급  \n

          **Returns**  \n
          새로운 accessToken  \n
          새로운 refreshToken  \n
          """)
  @PostMapping("/refresh")
  public ResponseEntity<BaseResponse<TokenResponse>> refresh(
      @Valid @RequestBody RefreshRequest request) {

    TokenResponse response = authService.refresh(request.getRefreshToken());

    return ResponseEntity.ok(BaseResponse.success(200, "토큰 재발급 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 로그아웃",
      description =
          """
          **Note**  \n
          - Stateless 구조 (서버 세션 없음)  \n
          - 클라이언트에서 저장된 토큰을 삭제하여 로그아웃 처리  \n
          """)
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout() {
    authService.logout();

    return ResponseEntity.ok(BaseResponse.success(200, "로그아웃 성공", null));
  }
}
