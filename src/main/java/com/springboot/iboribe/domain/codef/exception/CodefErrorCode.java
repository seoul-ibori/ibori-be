package com.springboot.iboribe.domain.codef.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodefErrorCode implements BaseErrorCode {
  CODEF_BAD_REQUEST("CODEF4001", "CODEF 요청값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  CODEF_UNAUTHORIZED("CODEF4011", "CODEF 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),

  CODEF_FORBIDDEN("CODEF4031", "CODEF API 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

  CODEF_INVALID_RESPONSE("CODEF5021", "CODEF 응답 형식이 올바르지 않습니다.", HttpStatus.BAD_GATEWAY),
  CODEF_EMPTY_RESPONSE("CODEF5022", "CODEF 응답 데이터가 비어 있습니다.", HttpStatus.BAD_GATEWAY),

  CODEF_API_TIMEOUT("CODEF5041", "CODEF API 응답 시간이 초과되었습니다.", HttpStatus.GATEWAY_TIMEOUT),
  CODEF_API_REQUEST_FAIL("CODEF5023", "CODEF API 호출에 실패했습니다.", HttpStatus.BAD_GATEWAY);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
