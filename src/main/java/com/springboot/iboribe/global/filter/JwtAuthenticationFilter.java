package com.springboot.iboribe.global.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.iboribe.global.security.jwt.JwtProvider;
import com.springboot.iboribe.global.security.jwt.TokenType;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final UserDetailsService userDetailsService;

  private static final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();

    return pathMatcher.match("/api/auth/signup", uri)
        || pathMatcher.match("/api/auth/login", uri)
        || pathMatcher.match("/api/auth/demo-login", uri)
        || pathMatcher.match("/api/auth/refresh", uri)
        || pathMatcher.match("/api/auth/check-username", uri)
        || pathMatcher.match("/api/auth/check-family-code", uri)
        || pathMatcher.match("/api/codef/medical-records", uri)
        || pathMatcher.match("/api/codef/children/register", uri)
        || pathMatcher.match("/api/codef/children/list", uri)
        || pathMatcher.match("/swagger-ui/**", uri)
        || pathMatcher.match("/swagger-ui.html", uri)
        || pathMatcher.match("/v3/api-docs/**", uri)
        || pathMatcher.match("/error", uri);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String accessToken = jwtProvider.extractAccessToken(request);

      if (accessToken != null && jwtProvider.validateToken(accessToken, TokenType.ACCESS_TOKEN)) {
        String username = jwtProvider.getUsernameFromToken(accessToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException e) {
      SecurityContextHolder.clearContext();
      log.warn("[JWT] 만료된 Access Token");
      writeErrorResponse(response, 401, "Access Token이 만료되었습니다.");

    } catch (JwtException | IllegalArgumentException e) {
      SecurityContextHolder.clearContext();
      log.warn("[JWT] 유효하지 않은 Access Token");
      writeErrorResponse(response, 401, "유효하지 않은 Access Token입니다.");

    } catch (UsernameNotFoundException e) {
      SecurityContextHolder.clearContext();
      log.warn("[JWT] 토큰의 사용자가 존재하지 않음 - username: {}", e.getMessage());
      filterChain.doFilter(request, response);
    }
  }

  private void writeErrorResponse(HttpServletResponse response, int status, String message)
      throws IOException {
    if (response.isCommitted()) {
      return;
    }

    response.setStatus(status);
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    response
        .getWriter()
        .write("{\"success\":false,\"code\":" + status + ",\"message\":\"" + message + "\"}");
    response.getWriter().flush();
  }
}
