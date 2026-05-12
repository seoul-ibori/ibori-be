package com.springboot.iboribe.domain.child.dto.response;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.entity.ChildProfileColor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ChildResponse: 자녀 목록 조회 응답 DTO")
public class ChildResponse {

  @Schema(description = "자녀 ID", example = "1")
  private Long childId;

  @Schema(description = "자녀 이름", example = "박지안")
  private String childName;

  @Schema(description = "프로필 색상", example = "PINK")
  private ChildProfileColor profileColor;

  @Schema(description = "별명", example = "지안이")
  private String nickname;

  @Schema(description = "생년월일 (yyyyMMdd)", example = "20200101")
  private String birthDate;

  @Schema(description = "키 (cm)", example = "110.5")
  private Double height;

  @Schema(description = "몸무게 (kg)", example = "18.3")
  private Double weight;

  @Schema(description = "메모", example = "땅콩 알레르기 있음")
  private String memo;

  public static ChildResponse from(Child child) {
    return ChildResponse.builder()
        .childId(child.getId())
        .childName(child.getName())
        .profileColor(child.getProfileColor())
        .nickname(child.getNickname())
        .birthDate(child.getBirthDate())
        .height(child.getHeight())
        .weight(child.getWeight())
        .memo(child.getMemo())
        .build();
  }
}
