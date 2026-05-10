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

  public static ChildResponse from(Child child) {
    return ChildResponse.builder()
        .childId(child.getId())
        .childName(child.getName())
        .profileColor(child.getProfileColor())
        .build();
  }
}
