package com.springboot.iboribe.domain.user.dto.response;

import com.springboot.iboribe.domain.family.entity.FamilyRole;
import com.springboot.iboribe.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private Long userId;
  private String name;
  private String username;
  private Long familyId;
  private String familyCode;
  private FamilyRole familyRole;

  public static UserInfoResponse from(User user) {
    return UserInfoResponse.builder()
        .userId(user.getId())
        .name(user.getName())
        .username(user.getUsername())
        .familyId(user.getFamily().getId())
        .familyCode(user.getFamily().getFamilyCode())
        .familyRole(user.getFamilyRole())
        .build();
  }
}
