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

  public DistrictResponse(String gu, String dong) {
    this.gu = gu;
    this.dong = dong;
    this.displayName = "서울시 " + gu + " " + dong;
  }
}
