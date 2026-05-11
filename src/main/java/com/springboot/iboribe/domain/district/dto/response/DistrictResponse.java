package com.springboot.iboribe.domain.district.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "DistrictResponse: 동 검색 응답 DTO")
public class DistrictResponse {

  @Schema(description = "구 이름", example = "송파구")
  private final String gu;

  @Schema(description = "동 이름", example = "잠실동")
  private final String dong;

  @Schema(description = "표시 이름", example = "서울시 송파구 잠실동")
  private final String displayName;

  @Schema(description = "동 대표 위도", example = "37.5133")
  private final Double lat;

  @Schema(description = "동 대표 경도", example = "127.1001")
  private final Double lng;

  public DistrictResponse(String gu, String dong, Double lat, Double lng) {
    this.gu = gu;
    this.dong = dong;
    this.displayName = "서울시 " + gu + " " + dong;
    this.lat = lat;
    this.lng = lng;
  }
}
