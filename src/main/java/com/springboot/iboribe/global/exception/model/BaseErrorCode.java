package com.springboot.iboribe.global.exception.model;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode { // 에러코드에 대한 최소한의 규격

  String getCode();

  String getMessage();

  HttpStatus getStatus();
}
