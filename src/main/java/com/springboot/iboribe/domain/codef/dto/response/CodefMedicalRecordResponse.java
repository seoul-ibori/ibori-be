package com.springboot.iboribe.domain.codef.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CodefMedicalRecordResponse: CODEF 의료 기록 응답 DTO")
public class CodefMedicalRecordResponse {

  @Schema(description = "조회 대상 이름 (본인 또는 자녀 이름)", example = "박지안")
  private String subjectName;

  @Schema(description = "병원 또는 약국 이름", example = "엠메디칼약국[의정부시 충의로]")
  private String hospitalName;

  @Schema(description = "진료 시작일", example = "20260331")
  private String treatDate;

  @Schema(description = "진료 유형", example = "처방조제")
  private String treatType;

  @Schema(description = "투약 정보 목록")
  private List<CodefMedicationResponse> medications;
}
