package com.springboot.iboribe.domain.medicalrecord.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MedicalRecordErrorCode implements BaseErrorCode {
  CODEF_RECORD_NOT_MODIFIABLE("RECORD4003", "CODEF 데이터는 수정하거나 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

  MEDICAL_RECORD_NOT_FOUND("RECORD4041", "의료 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
