package com.springboot.iboribe.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.springboot.iboribe.global.config.property.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties jwtProperties;
  private static final String COOKIE_NAME = "accessToken";

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(Long userId, String username) {
    return generateToken(
        userId, username, TokenType.ACCESS_TOKEN, jwtProperties.getAccessTokenValidityInSeconds());
  }

  public String generateRefreshToken(Long userId, String username) {
    return generateToken(
        userId,
        username,
        TokenType.REFRESH_TOKEN,
        jwtProperties.getRefreshTokenValidityInSeconds());
  }

  private String generateToken(
      Long userId, String username, TokenType tokenType, long validitySeconds) {
    Instant now = Instant.now();

    return Jwts.builder()
        .subject(username)
        .claim("userId", userId)
        .claim("type", tokenType.name())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(validitySeconds)))
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateToken(String token, TokenType tokenType) {
    Claims claims = extractClaims(token);
    String type = claims.get("type", String.class);

    if (!tokenType.name().equals(type)) {
      throw new JwtException("토큰 타입이 일치하지 않습니다.");
    }

    return true;
  }

  public Long getUserIdFromToken(String token) {
    return extractClaims(token).get("userId", Long.class);
  }

  public String getUsernameFromToken(String token) {
    return extractClaims(token).getSubject();
  }

  private Claims extractClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public ResponseCookie createAccessTokenCookie(String token) {
    return ResponseCookie.from(COOKIE_NAME, token)
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .path("/")
        .maxAge(Duration.ofSeconds(jwtProperties.getAccessTokenValidityInSeconds()))
        .sameSite(jwtProperties.getSameSite())
        .build();
  }

  public ResponseCookie clearAccessTokenCookie() {
    return ResponseCookie.from(COOKIE_NAME, "")
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .path("/")
        .maxAge(0)
        .sameSite(jwtProperties.getSameSite())
        .build();
  }

  public String extractAccessToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return null;

    for (Cookie cookie : cookies) {
      if (COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
