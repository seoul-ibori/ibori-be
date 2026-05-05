package com.springboot.iboribe.domain.hospital.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HospitalErrorCode implements BaseErrorCode {
  HOSPITAL_NOT_FOUND("HOSPITAL4041", "존재하지 않는 병원입니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
