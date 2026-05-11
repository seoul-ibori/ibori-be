package com.springboot.iboribe.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.auth.dto.request.LoginRequest;
import com.springboot.iboribe.domain.auth.dto.request.SignUpRequest;
import com.springboot.iboribe.domain.auth.dto.response.DuplicateCheckResponse;
import com.springboot.iboribe.domain.auth.dto.response.TokenResponse;
import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.repository.FamilyRepository;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;
import com.springboot.iboribe.global.security.jwt.JwtProvider;
import com.springboot.iboribe.global.security.jwt.TokenType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final FamilyRepository familyRepository;
  private static final String DEMO_USERNAME = "mom_jiyoon";

  @Override
  @Transactional(readOnly = true)
  public TokenResponse demoLogin() {
    User user =
        userRepository
            .findByUsername(DEMO_USERNAME)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    log.info("[Auth] 데모 계정 로그인 성공 - userId: {}, username: {}", user.getId(), user.getUsername());

    return createTokenResponse(user);
  }

  @Override
  public TokenResponse signUp(SignUpRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new CustomException(AuthErrorCode.ALREADY_EXIST_USERNAME);
    }

    Family family;

    if (request.getFirstFamilyMember()) {
      if (familyRepository.existsByFamilyCode(request.getFamilyCode())) {
        throw new CustomException(AuthErrorCode.ALREADY_EXIST_FAMILY_CODE);
      }

      family = familyRepository.save(Family.builder().familyCode(request.getFamilyCode()).build());
    } else {
      family =
          familyRepository
              .findByFamilyCode(request.getFamilyCode())
              .orElseThrow(() -> new CustomException(AuthErrorCode.FAMILY_NOT_FOUND));
    }

    User user =
        userRepository.save(
            User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .family(family)
                .familyRole(request.getFamilyRole())
                .build());

    log.info("[Auth] 회원가입 성공 - userId: {}, username: {}", user.getId(), user.getUsername());

    return createTokenResponse(user);
  }

  @Override
  @Transactional(readOnly = true)
  public TokenResponse login(LoginRequest request) {
    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(
                () -> {
                  log.info("[Auth] 로그인 실패 - 존재하지 않는 아이디: {}", request.getUsername());
                  return new CustomException(AuthErrorCode.LOGIN_FAIL);
                });

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      log.info("[Auth] 로그인 실패 - 비밀번호 불일치, username: {}", request.getUsername());
      throw new CustomException(AuthErrorCode.LOGIN_FAIL);
    }

    TokenResponse tokenResponse = createTokenResponse(user);

    log.info("[Auth] 사용자 로그인 성공 - userId: {}, username: {}", user.getId(), user.getUsername());

    return tokenResponse;
  }

  @Override
  @Transactional(readOnly = true)
  public TokenResponse refresh(String refreshToken) {
    if (refreshToken == null) {
      log.warn("[Auth] 토큰 재발급 실패 - Refresh Token 없음");
      throw new CustomException(AuthErrorCode.REFRESH_TOKEN_REQUIRED);
    }

    if (!jwtProvider.validateToken(refreshToken, TokenType.REFRESH_TOKEN)) {
      log.warn("[Auth] 토큰 재발급 실패 - 유효하지 않은 Refresh Token");
      throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    Long userId = jwtProvider.getUserIdFromToken(refreshToken);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> {
                  log.warn("[Auth] 토큰 재발급 실패 - 사용자를 찾을 수 없음, userId: {}", userId);
                  return new CustomException(AuthErrorCode.USER_NOT_FOUND);
                });

    TokenResponse tokenResponse = createTokenResponse(user);

    log.info("[Auth] 토큰 재발급 성공 - userId: {}, username: {}", user.getId(), user.getUsername());

    return tokenResponse;
  }

  @Override
  public void logout() {
    log.info("[Auth] 사용자 로그아웃 요청 - 쿠키 만료 처리");
  }

  @Override
  @Transactional(readOnly = true)
  public DuplicateCheckResponse checkUsername(String username) {
    boolean isDuplicate = userRepository.existsByUsername(username);
    return DuplicateCheckResponse.of(isDuplicate);
  }

  @Override
  @Transactional(readOnly = true)
  public DuplicateCheckResponse checkFamilyCode(String familyCode) {
    boolean isDuplicate = familyRepository.existsByFamilyCode(familyCode);
    return DuplicateCheckResponse.of(isDuplicate);
  }

  private TokenResponse createTokenResponse(User user) {
    String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getUsername());
    String refreshToken = jwtProvider.generateRefreshToken(user.getId(), user.getUsername());

    return TokenResponse.builder()
        .userId(user.getId())
        .name(user.getName())
        .username(user.getUsername())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
