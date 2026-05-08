package com.springboot.iboribe.domain.family.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateFamilyCodeRequest {

  @NotBlank(message = "가족고유번호는 필수입니다.")
  @Size(min = 8, max = 30, message = "가족고유번호는 8자 이상 30자 이하로 입력해야 합니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,30}$",
      message = "가족고유번호는 8자 이상이며 영문자와 특수문자를 포함해야 합니다.")
  @Schema(description = "수정할 가족고유번호", example = "NewFamily!123")
  private String familyCode;
}
