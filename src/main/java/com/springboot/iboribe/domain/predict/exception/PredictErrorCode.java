package com.springboot.iboribe.domain.predict.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PredictErrorCode implements BaseErrorCode {
  PREDICT_API_TIMEOUT("PREDICT5031", "예측 서버 응답 시간이 초과되었습니다.", HttpStatus.GATEWAY_TIMEOUT),
  PREDICT_API_ERROR("PREDICT5021", "예측 서버 호출 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
