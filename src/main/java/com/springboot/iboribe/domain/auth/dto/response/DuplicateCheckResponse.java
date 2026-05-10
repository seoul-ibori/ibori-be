package com.springboot.iboribe.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateCheckResponse {

  @Schema(description = "사용 가능 여부 (true = 사용 가능, false = 이미 사용 중)", example = "true")
  private final boolean available;

  public static DuplicateCheckResponse of(boolean isDuplicate) {
    return new DuplicateCheckResponse(!isDuplicate);
  }
}
