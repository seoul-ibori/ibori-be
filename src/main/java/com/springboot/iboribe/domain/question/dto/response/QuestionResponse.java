package com.springboot.iboribe.domain.question.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "AI 질문지 응답")
public class QuestionResponse {

  @Schema(description = "AI가 생성한 소아과 진료 전 확인 질문 목록")
  private List<String> questions;
}
