package com.springboot.iboribe.global.security.jwt;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.springboot.iboribe.global.config.property.JwtProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtCookieWriter {

  private final JwtProperties jwtProperties;

  public ResponseCookie addAccessTokenToCookie(String accessToken) {
    return ResponseCookie.from(TokenType.ACCESS_TOKEN.name(), accessToken)
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .sameSite(jwtProperties.getSameSite())
        .path("/")
        .maxAge(jwtProperties.getAccessTokenValidityInSeconds())
        .build();
  }

  public ResponseCookie addRefreshTokenToCookie(String refreshToken) {
    return ResponseCookie.from(TokenType.REFRESH_TOKEN.name(), refreshToken)
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .sameSite(jwtProperties.getSameSite())
        .path("/")
        .maxAge(jwtProperties.getRefreshTokenValidityInSeconds())
        .build();
  }

  public ResponseCookie removeTokenFromCookie(TokenType tokenType) {
    return ResponseCookie.from(tokenType.name(), "")
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .sameSite(jwtProperties.getSameSite())
        .path("/")
        .maxAge(0)
        .build();
  }
}
