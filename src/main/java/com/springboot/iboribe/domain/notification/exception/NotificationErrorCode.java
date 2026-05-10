package com.springboot.iboribe.domain.notification.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements BaseErrorCode {
  NOTIFICATION_NOT_FOUND("NOTIF4041", "알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
