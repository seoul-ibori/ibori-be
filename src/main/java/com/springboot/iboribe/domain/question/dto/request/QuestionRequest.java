package com.springboot.iboribe.domain.question.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "AI 질문지 생성 요청")
public class QuestionRequest {

  @NotEmpty
  @Schema(description = "선택한 증상 목록", example = "[\"인후통이 있어요\", \"기침을 해요\"]")
  private List<String> symptoms;

  @NotBlank
  @Schema(description = "증상 시작 기간", example = "3")
  private String symptomDuration;

  @Schema(description = "체온 (°C)", example = "38.5")
  private Double temperature;

  @Schema(description = "식욕 변화", example = "나쁨")
  private String appetiteChange;

  @Schema(description = "수면 상태", example = "보통")
  private String sleepCondition;

  @Schema(description = "약 복용 특이점", example = "[\"알약을 못 삼켜요\", \"물약을 못 먹어요\"]")
  private List<String> medicationNotes;
}
