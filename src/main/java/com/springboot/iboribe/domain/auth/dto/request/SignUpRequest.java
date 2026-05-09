package com.springboot.iboribe.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.springboot.iboribe.domain.family.entity.FamilyRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "SignUpRequest: 회원가입 요청 DTO")
public class SignUpRequest {

  @NotBlank(message = "이름은 필수입니다.")
  @Schema(description = "사용자 이름", example = "김서연")
  private String name;

  @NotBlank(message = "아이디는 필수입니다.")
  @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해야 합니다.")
  @Schema(description = "사용자 아이디", example = "mom_seoyeon")
  private String username;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,20}$",
      message = "비밀번호는 8~20자이며, 영문/숫자/특수문자를 모두 포함해야 합니다.")
  @Schema(description = "사용자 비밀번호", example = "BabyCare123!")
  private String password;

  @NotBlank(message = "가족고유번호는 필수입니다.")
  @Size(min = 8, max = 20, message = "가족고유번호는 8자 이상 20자 이하로 입력해야 합니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,20}$",
      message = "가족고유번호는 8자 이상이며 영문자와 특수문자를 포함해야 합니다.")
  @Schema(description = "가족고유번호", example = "Family!123")
  private String familyCode;

  @NotNull(message = "가족 첫 가입자 여부는 필수입니다.")
  @Schema(description = "가족 중 첫 가입자 여부", example = "true")
  private Boolean firstFamilyMember;

  @NotNull(message = "가족 구성원 역할은 필수입니다.")
  @Schema(description = "가족 구성원 역할", example = "MOTHER")
  private FamilyRole familyRole;
}
