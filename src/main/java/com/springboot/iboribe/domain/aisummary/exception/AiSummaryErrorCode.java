package com.springboot.iboribe.domain.aisummary.exception;

import org.springframework.http.HttpStatus;

import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiSummaryErrorCode implements BaseErrorCode {
  AUDIO_FILE_EMPTY("AISUM4001", "음성 파일이 비어 있습니다.", HttpStatus.BAD_REQUEST),
  AI_SUMMARY_ALREADY_EXISTS("AISUM4002", "이미 AI 요약이 존재합니다.", HttpStatus.BAD_REQUEST),
  CHILD_ID_REQUIRED("AISUM4003", "진료 기록 없이 요약 시 자녀 ID가 필요합니다.", HttpStatus.BAD_REQUEST),

  AI_SUMMARY_NOT_FOUND("AISUM4041", "AI 요약을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  TRANSCRIPTION_FAILED("AISUM5021", "음성 파일 처리 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
  SUMMARY_GENERATION_FAILED("AISUM5022", "AI 요약 생성 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
