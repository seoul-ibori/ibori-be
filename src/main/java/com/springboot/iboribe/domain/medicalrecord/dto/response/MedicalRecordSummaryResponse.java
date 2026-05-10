package com.springboot.iboribe.domain.medicalrecord.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecordSource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@Schema(title = "MedicalRecordSummaryResponse: 의료 기록 목록 응답 DTO")
public class MedicalRecordSummaryResponse {

  @Schema(description = "의료 기록 ID", example = "10")
  private Long recordId;

  @Schema(description = "자녀 이름", example = "박지안")
  private String childName;

  @Schema(description = "진료 제목", example = "소아과 정기 검진")
  private String title;

  @Schema(description = "진료 날짜", example = "20260510")
  private String treatDate;

  @Schema(description = "진료 시간", example = "14:30")
  private String treatTime;

  @Schema(description = "병원명", example = "삼성튼튼소아청소년과의원")
  private String hospitalName;

  @Schema(description = "메모", example = "예방접종 예정")
  private String memo;

  @Schema(description = "AI 요약 존재 여부", example = "false")
  private boolean hasAiSummary;

  @Schema(description = "투약 상세 정보 목록")
  private List<MedicationDetailResponse> medications;

  public static MedicalRecordSummaryResponse from(MedicalRecord record) {

    boolean isUserRecord = record.getSource() == MedicalRecordSource.USER;

    return MedicalRecordSummaryResponse.builder()
        .recordId(record.getId())
        .childName(record.getChild().getName())
        .title(record.getTitle())
        .treatDate(record.getTreatDate())
        .treatTime(isUserRecord ? record.getTreatTime() : null)
        .hospitalName(record.getHospitalName())
        .memo(isUserRecord ? record.getMemo() : null)
        .hasAiSummary(record.getAiSummary() != null)
        .medications(
            record.getMedications() != null
                ? record.getMedications().stream().map(MedicationDetailResponse::from).toList()
                : List.of())
        .build();
  }
}
