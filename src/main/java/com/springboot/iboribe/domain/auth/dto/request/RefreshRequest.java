package com.springboot.iboribe.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "RefreshRequest: 토큰 재발급 요청 DTO")
public class RefreshRequest {

  @NotBlank(message = "Refresh Token은 필수입니다.")
  @Schema(description = "Refresh Token")
  private String refreshToken;
}
