package com.springboot.iboribe.domain.predict.dto.response;

import com.springboot.iboribe.domain.predict.entity.CongestionLevel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "PredictResponse: 혼잡도 예측 응답 DTO")
public class PredictResponse {

  @Schema(description = "구 이름", example = "강남구")
  private String gu;

  @Schema(description = "동 이름", example = "역삼동")
  private String dong;

  @Schema(description = "혼잡도", example = "보통")
  private CongestionLevel congestionLevel;
}
