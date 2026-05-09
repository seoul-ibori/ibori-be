package com.springboot.iboribe.domain.medicalrecord.dto.response;

import com.springboot.iboribe.domain.codef.dto.response.CodefMedicationResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MedicationDetailResponse: 투약 상세 응답 DTO")
public class MedicationDetailResponse {

  @Schema(description = "처방 약 이름", example = "시네츄라시럽(500mL)")
  private String drugName;

  @Schema(description = "약 효능", example = "진해거담제")
  private String drugEffect;

  @Schema(description = "처방 일수", example = "4")
  private String prescribeDays;

  public static MedicationDetailResponse from(CodefMedicationResponse medication) {
    return MedicationDetailResponse.builder()
        .drugName(medication.getDrugName())
        .drugEffect(medication.getDrugEffect())
        .prescribeDays(medication.getPrescribeDays())
        .build();
  }
}
