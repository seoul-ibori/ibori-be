package com.springboot.iboribe.domain.family.dto.response;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.family.entity.FamilyRole;
import com.springboot.iboribe.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "FamilyMemberResponse: 가족 구성원 조회 응답 DTO")
public class FamilyMemberResponse {

  @Schema(description = "구성원 ID", example = "1")
  private Long memberId;

  @Schema(description = "구성원 이름", example = "김지윤")
  private String name;

  @Schema(description = "사용자 아이디, 자녀인 경우 null", example = "mom_jiyoon")
  private String username;

  @Schema(description = "가족 역할, 자녀인 경우 null", example = "MOTHER")
  private FamilyRole familyRole;

  @Schema(description = "구성원 타입, USER 또는 CHILD", example = "USER")
  private String memberType;

  public static FamilyMemberResponse fromUser(User user) {
    return FamilyMemberResponse.builder()
        .memberId(user.getId())
        .name(user.getName())
        .username(user.getUsername())
        .familyRole(user.getFamilyRole())
        .memberType("USER")
        .build();
  }

  public static FamilyMemberResponse fromChild(Child child) {
    return FamilyMemberResponse.builder()
        .memberId(child.getId())
        .name(child.getName())
        .username(null)
        .familyRole(child.getFamilyRole())
        .memberType("CHILD")
        .build();
  }
}
