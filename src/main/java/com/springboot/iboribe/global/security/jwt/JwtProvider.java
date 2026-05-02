package com.springboot.iboribe.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

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

  public String extractAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (TokenType.ACCESS_TOKEN.name().equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }

  public String extractRefreshToken(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (TokenType.REFRESH_TOKEN.name().equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }
}
