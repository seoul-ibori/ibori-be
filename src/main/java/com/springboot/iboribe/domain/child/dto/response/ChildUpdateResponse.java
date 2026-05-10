package com.springboot.iboribe.domain.child.dto.response;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.entity.ChildProfileColor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ChildUpdateResponse: 아이 정보 수정 응답 DTO")
public class ChildUpdateResponse {

  @Schema(description = "자녀 ID", example = "1")
  private Long childId;

  @Schema(description = "자녀 이름", example = "박지안")
  private String name;

  @Schema(description = "프로필 색상", example = "PINK")
  private ChildProfileColor profileColor;

  @Schema(description = "생년월일 (yyyyMMdd)", example = "20201015")
  private String birthDate;

  @Schema(description = "닉네임", example = "지안이")
  private String nickname;

  @Schema(description = "키 (cm)", example = "115.5")
  private Double height;

  @Schema(description = "몸무게 (kg)", example = "20.3")
  private Double weight;

  @Schema(description = "메모", example = "땅콩 알레르기 있음")
  private String memo;

  public static ChildUpdateResponse from(Child child) {
    return ChildUpdateResponse.builder()
        .childId(child.getId())
        .name(child.getName())
        .profileColor(child.getProfileColor())
        .birthDate(child.getBirthDate())
        .nickname(child.getNickname())
        .height(child.getHeight())
        .weight(child.getWeight())
        .memo(child.getMemo())
        .build();
  }
}
