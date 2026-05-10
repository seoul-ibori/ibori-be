package com.springboot.iboribe.domain.auth.service;

import com.springboot.iboribe.domain.auth.dto.request.LoginRequest;
import com.springboot.iboribe.domain.auth.dto.request.SignUpRequest;
import com.springboot.iboribe.domain.auth.dto.response.DuplicateCheckResponse;
import com.springboot.iboribe.domain.auth.dto.response.TokenResponse;

public interface AuthService {

  /** 데모 계정으로 로그인 */
  TokenResponse demoLogin();

  /** 회원가입 */
  void signUp(SignUpRequest request);

  /** 로그인 - 토큰 발급 */
  TokenResponse login(LoginRequest request);

  /** 토큰 재발급 */
  TokenResponse refresh(String refreshToken);

  /** 로그아웃 */
  void logout();

  /** 아이디 중복 확인 */
  DuplicateCheckResponse checkUsername(String username);

  /** 가족고유번호 중복 확인 */
  DuplicateCheckResponse checkFamilyCode(String familyCode);
}
