package com.springboot.iboribe.domain.question.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements BaseErrorCode {
  GPT_API_TIMEOUT("QUESTION5031", "AI 서버 응답 시간이 초과되었습니다.", HttpStatus.GATEWAY_TIMEOUT),
  GPT_API_ERROR("QUESTION5021", "AI 서버 호출 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
  GPT_INVALID_RESPONSE(
      "QUESTION5001", "AI 응답을 파싱하는 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
