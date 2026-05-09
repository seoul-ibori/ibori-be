package com.springboot.iboribe.domain.family.service;

import java.util.List;

import com.springboot.iboribe.domain.family.dto.request.UpdateFamilyCodeRequest;
import com.springboot.iboribe.domain.family.dto.response.FamilyMemberResponse;

public interface FamilyService {

  List<FamilyMemberResponse> getFamilyMembers(Long loginUserId);

  void deleteFamilyMember(Long loginUserId, Long memberId);

  void updateFamilyCode(Long loginUserId, UpdateFamilyCodeRequest request);
}
