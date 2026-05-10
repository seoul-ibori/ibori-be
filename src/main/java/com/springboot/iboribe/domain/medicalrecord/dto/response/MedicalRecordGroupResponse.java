package com.springboot.iboribe.domain.medicalrecord.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MedicalRecordGroupResponse: 아이별 의료 기록 그룹 응답 DTO")
public class MedicalRecordGroupResponse {

  @Schema(description = "자녀 ID", example = "1")
  private Long childId;

  @Schema(description = "자녀 이름", example = "박지안")
  private String childName;

  @Schema(description = "해당 자녀의 의료 기록 목록")
  private List<MedicalRecordSummaryResponse> records;
}
