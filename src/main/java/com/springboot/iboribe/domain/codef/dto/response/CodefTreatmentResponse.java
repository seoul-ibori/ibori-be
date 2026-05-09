package com.springboot.iboribe.domain.codef.dto.response;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CodefTreatmentResponse: CODEF 의료 기록 조회 응답 DTO")
public class CodefTreatmentResponse {

  @Schema(description = "CODEF 결과 코드", example = "CF-00000")
  private String resultCode;

  @Schema(description = "CODEF 결과 메시지", example = "성공")
  private String resultMessage;

  @Schema(description = "추가 인증 필요 여부", example = "true")
  private Boolean continue2Way;

  @Schema(description = "추가 인증 방식", example = "simpleAuth")
  private String method;

  @Schema(description = "2차 요청에 필요한 추가 인증 정보")
  private Map<String, Object> twoWayInfo;

  @Schema(description = "정제된 의료 기록 목록")
  private List<CodefMedicalRecordResponse> medicalRecords;

  @Schema(description = "CODEF 원본 응답 데이터")
  private Map<String, Object> rawData;
}
