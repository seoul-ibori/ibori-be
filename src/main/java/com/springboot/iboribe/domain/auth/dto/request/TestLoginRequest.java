package com.springboot.iboribe.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class TestLoginRequest {

  @NotBlank(message = "아이디는 필수입니다.")
  private String username;
}
