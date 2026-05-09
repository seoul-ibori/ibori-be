package com.springboot.iboribe.domain.medicalrecord.dto.response;

import java.util.List;

import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MedicalRecordDetailResponse: 의료 기록 상세 응답 DTO")
public class MedicalRecordDetailResponse {

  @Schema(description = "의료 기록 ID", example = "10")
  private Long recordId;

  @Schema(description = "자녀 이름", example = "박지안")
  private String childName;

  @Schema(description = "진료 또는 조제 날짜", example = "20260331")
  private String treatDate;

  @Schema(description = "병원 또는 약국 이름", example = "엠메디칼약국[의정부시 충의로]")
  private String hospitalName;

  @Schema(description = "진료 유형", example = "처방조제")
  private String treatType;

  @Schema(description = "투약 상세 정보 목록")
  private List<MedicationDetailResponse> medications;

  public static MedicalRecordDetailResponse from(MedicalRecord record) {
    return MedicalRecordDetailResponse.builder()
        .recordId(record.getId())
        .childName(record.getChild().getName())
        .treatDate(record.getTreatDate())
        .hospitalName(record.getHospitalName())
        .treatType(record.getTreatType())
        .medications(record.getMedications().stream().map(MedicationDetailResponse::from).toList())
        .build();
  }
}
