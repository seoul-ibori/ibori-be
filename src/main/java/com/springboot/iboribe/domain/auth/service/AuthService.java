package com.springboot.iboribe.domain.auth.service;

import com.springboot.iboribe.domain.auth.dto.request.LoginRequest;
import com.springboot.iboribe.domain.auth.dto.request.SignUpRequest;
import com.springboot.iboribe.domain.auth.dto.response.TokenResponse;

public interface AuthService {

  /** 회원가입 */
  void signUp(SignUpRequest request);

  /** 로그인 - 토큰 발급 */
  TokenResponse login(LoginRequest request);

  /** 토큰 재발급 */
  TokenResponse refresh(String refreshToken);

  /** 로그아웃 */
  void logout();
}
