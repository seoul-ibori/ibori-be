package com.springboot.iboribe.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
  ALREADY_EXIST_USERNAME("AUTH4001", "이미 사용 중인 아이디입니다.", HttpStatus.BAD_REQUEST),
  LOGIN_FAIL("AUTH4002", "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  INVALID_INPUT_FORMAT("AUTH4003", "아이디 또는 비밀번호 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

  REFRESH_TOKEN_REQUIRED("AUTH4011", "리프레시 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED),
  INVALID_REFRESH_TOKEN("AUTH4012", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),

  USER_NOT_FOUND("AUTH4041", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
