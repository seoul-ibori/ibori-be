package com.springboot.iboribe.domain.family.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FamilyErrorCode implements BaseErrorCode {
  ALREADY_EXIST_FAMILY_CODE("FAMILY4001", "이미 사용 중인 가족고유번호입니다.", HttpStatus.BAD_REQUEST),
  CANNOT_DELETE_SELF("FAMILY4002", "본인은 가족 구성원에서 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

  NOT_SAME_FAMILY("FAMILY4031", "같은 가족 구성원만 접근할 수 있습니다.", HttpStatus.FORBIDDEN),

  FAMILY_NOT_FOUND("FAMILY4041", "가족 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  FAMILY_MEMBER_NOT_FOUND("FAMILY4042", "가족 구성원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
