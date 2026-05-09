package com.springboot.iboribe.domain.medicalrecord.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "MedicalRecordUpdateRequest: 사용자 진료 기록 수정 요청 DTO")
public class MedicalRecordUpdateRequest {

  @Schema(description = "자녀 ID — null이면 기존 자녀 유지", example = "1")
  private Long childId;

  @Schema(description = "진료 제목 — null이면 기존 값 유지", example = "소아과 정기 검진")
  private String title;

  @Schema(description = "병원명 — null이면 기존 값 유지", example = "삼성튼튼소아청소년과의원")
  private String hospitalName;

  @Schema(description = "진료 시간 HH:mm — null이면 기존 값 유지", example = "14:30")
  private String treatTime;

  @Schema(description = "메모 — null이면 기존 값 유지", example = "예방접종 예정")
  private String memo;
}
