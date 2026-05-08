package com.springboot.iboribe.domain.medicalrecord.dto.response;

import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MedicalRecordSummaryResponse: 의료 기록 요약 응답 DTO")
public class MedicalRecordSummaryResponse {

  @Schema(description = "의료 기록 ID", example = "10")
  private Long recordId;

  @Schema(description = "진료 또는 조제 날짜", example = "20260331")
  private String treatDate;

  @Schema(description = "병원 또는 약국 이름", example = "엠메디칼약국[의정부시 충의로]")
  private String hospitalName;

  @Schema(description = "진료 유형", example = "처방조제")
  private String treatType;

  public static MedicalRecordSummaryResponse from(MedicalRecord record) {
    return MedicalRecordSummaryResponse.builder()
        .recordId(record.getId())
        .treatDate(record.getTreatDate())
        .hospitalName(record.getHospitalName())
        .treatType(record.getTreatType())
        .build();
  }
}
