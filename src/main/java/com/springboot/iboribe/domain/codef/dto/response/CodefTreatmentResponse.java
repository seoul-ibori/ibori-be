package com.springboot.iboribe.domain.codef.dto.response;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CodefTreatmentResponse: CODEF 진료 및 투약정보 응답 DTO")
public class CodefTreatmentResponse {

  private String resultCode;
  private String resultMessage;

  private Boolean continue2Way;
  private String method;
  private Map<String, Object> twoWayInfo;

  @Schema(description = "진료 및 투약정보 목록")
  private List<Map<String, Object>> treatments;

  @Schema(description = "CODEF 원본 응답 데이터")
  private Map<String, Object> rawData;
}
