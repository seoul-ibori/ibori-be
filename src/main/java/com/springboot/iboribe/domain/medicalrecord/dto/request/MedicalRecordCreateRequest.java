package com.springboot.iboribe.domain.medicalrecord.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "MedicalRecordCreateRequest: 사용자 직접 진료 기록 생성 요청 DTO")
public class MedicalRecordCreateRequest {

  @NotNull(message = "자녀 ID는 필수입니다.")
  @Schema(description = "자녀 ID", example = "1")
  private Long childId;

  @NotBlank(message = "진료 제목은 필수입니다.")
  @Schema(description = "진료 제목", example = "소아과 정기 검진")
  private String title;

  @NotBlank(message = "병원명은 필수입니다.")
  @Schema(description = "병원명", example = "삼성튼튼소아청소년과의원")
  private String hospitalName;

  @NotBlank(message = "진료 날짜는 필수입니다.")
  @Schema(description = "진료 날짜 yyyyMMdd", example = "20260510")
  private String treatDate;

  @NotBlank(message = "진료 시간은 필수입니다.")
  @Schema(description = "진료 시간 HH:mm", example = "14:30")
  private String treatTime;

  @Schema(description = "메모", example = "예방접종 예정")
  private String memo;
}
