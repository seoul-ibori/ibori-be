package com.springboot.iboribe.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "LoginRequest: 로그인 요청 DTO")
public class LoginRequest {

  @NotBlank
  @Schema(description = "사용자 아이디", example = "mom_seoyeon")
  private String username;

  @NotBlank
  @Schema(description = "사용자 비밀번호", example = "BabyCare123!")
  private String password;
}
