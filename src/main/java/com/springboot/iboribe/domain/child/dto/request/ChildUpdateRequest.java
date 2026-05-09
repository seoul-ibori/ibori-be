package com.springboot.iboribe.domain.child.dto.request;

import jakarta.validation.constraints.Size;

import com.springboot.iboribe.domain.child.entity.ChildProfileColor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "ChildUpdateRequest: 아이 정보 수정 요청 DTO")
public class ChildUpdateRequest {

  @Schema(description = "프로필 색상", example = "PINK")
  private ChildProfileColor profileColor;

  @Schema(description = "생년월일 (yyyyMMdd)", example = "20201015")
  @Size(min = 8, max = 8, message = "생년월일은 8자리 yyyyMMdd 형식이어야 합니다.")
  private String birthDate;

  @Schema(description = "닉네임", example = "지안이")
  @Size(max = 30, message = "닉네임은 30자 이하여야 합니다.")
  private String nickname;

  @Schema(description = "키 (cm)", example = "115.5")
  private Double height;

  @Schema(description = "몸무게 (kg)", example = "20.3")
  private Double weight;

  @Schema(description = "메모", example = "땅콩 알레르기 있음")
  private String memo;
}
