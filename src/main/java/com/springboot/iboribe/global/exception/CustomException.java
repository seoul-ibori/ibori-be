package com.springboot.iboribe.global.exception;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException { // 에러를 감지해서 핸들러에게 던진다

  private final BaseErrorCode errorCode;

  public CustomException(BaseErrorCode errorCode) {
    super(errorCode.getMessage()); // RuntimeException의 내부 message 저장
    this.errorCode = errorCode;
  }
}
