package com.springboot.iboribe.domain.predict.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "PredictRequest: 혼잡도 예측 요청 DTO")
public class PredictRequest {

  @NotBlank
  @Schema(description = "동 이름", example = "역삼동")
  private String dong;
}
