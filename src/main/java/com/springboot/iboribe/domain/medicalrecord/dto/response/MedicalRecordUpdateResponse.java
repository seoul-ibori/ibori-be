package com.springboot.iboribe.domain.medicalrecord.dto.response;

import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MedicalRecordUpdateResponse: 사용자 진료 기록 수정 응답 DTO")
public class MedicalRecordUpdateResponse {

  @Schema(description = "진료 기록 ID", example = "13")
  private Long recordId;

  @Schema(description = "AI 요약 존재 여부", example = "false")
  private boolean hasAiSummary;

  @Schema(description = "자녀 ID", example = "1")
  private Long childId;

  @Schema(description = "진료 제목", example = "소아과 정기 검진")
  private String title;

  @Schema(description = "병원명", example = "삼성튼튼소아청소년과의원")
  private String hospitalName;

  @Schema(description = "진료 날짜", example = "20260515")
  private String treatDate;

  @Schema(description = "진료 시간", example = "14:30")
  private String treatTime;

  @Schema(description = "메모", example = "예방접종 예정")
  private String memo;

  public static MedicalRecordUpdateResponse from(
      MedicalRecord medicalRecord, boolean hasAiSummary) {
    return MedicalRecordUpdateResponse.builder()
        .recordId(medicalRecord.getId())
        .hasAiSummary(hasAiSummary)
        .childId(medicalRecord.getChild().getId())
        .title(medicalRecord.getTitle())
        .hospitalName(medicalRecord.getHospitalName())
        .treatDate(medicalRecord.getTreatDate())
        .treatTime(medicalRecord.getTreatTime())
        .memo(medicalRecord.getMemo())
        .build();
  }
}
