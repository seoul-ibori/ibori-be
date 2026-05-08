package com.springboot.iboribe.domain.codef.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "CodefMedicationResponse: CODEF 투약 정보 응답 DTO")
public class CodefMedicationResponse {

  @Schema(description = "진료 또는 처방 날짜", example = "20260331")
  private String treatDate;

  @Schema(description = "진료 상세 유형", example = "약국")
  private String treatTypeDetail;

  @Schema(description = "처방 약 이름", example = "시네츄라시럽(500mL) (Synatura Syrup(500mL))")
  private String drugName;

  @Schema(description = "약 효능", example = "진해거담제")
  private String drugEffect;

  @Schema(description = "처방 일수", example = "4")
  private String prescribeDays;
}
