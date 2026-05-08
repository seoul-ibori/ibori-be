package com.springboot.iboribe.domain.family.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.family.dto.request.UpdateFamilyCodeRequest;
import com.springboot.iboribe.domain.family.dto.response.FamilyMemberResponse;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.exception.FamilyErrorCode;
import com.springboot.iboribe.domain.family.repository.FamilyRepository;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilyServiceImpl implements FamilyService {

  private final UserRepository userRepository;
  private final FamilyRepository familyRepository;
  private final ChildRepository childRepository;

  @Override
  public List<FamilyMemberResponse> getFamilyMembers(Long loginUserId) {
    User loginUser = getUser(loginUserId);
    Family family = loginUser.getFamily();

    List<FamilyMemberResponse> responses = new ArrayList<>();

    responses.addAll(
        userRepository.findAllByFamily(family).stream()
            .map(FamilyMemberResponse::fromUser)
            .toList());

    responses.addAll(
        childRepository.findAllByFamily(family).stream()
            .map(FamilyMemberResponse::fromChild)
            .toList());

    return responses;
  }

  @Override
  @Transactional
  public void deleteFamilyMember(Long loginUserId, Long memberId) {
    if (loginUserId.equals(memberId)) {
      throw new CustomException(FamilyErrorCode.CANNOT_DELETE_SELF);
    }

    User loginUser = getUser(loginUserId);

    User targetUser =
        userRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(FamilyErrorCode.FAMILY_MEMBER_NOT_FOUND));

    validateSameFamily(targetUser, loginUser.getFamily());

    userRepository.delete(targetUser);
  }

  @Override
  @Transactional
  public void updateFamilyCode(Long loginUserId, UpdateFamilyCodeRequest request) {
    User loginUser = getUser(loginUserId);
    Family family = loginUser.getFamily();

    if (familyRepository.existsByFamilyCode(request.getFamilyCode())) {
      throw new CustomException(FamilyErrorCode.ALREADY_EXIST_FAMILY_CODE);
    }

    family.updateFamilyCode(request.getFamilyCode());
  }

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
  }

  private Family getFamily(Long familyId) {
    return familyRepository
        .findById(familyId)
        .orElseThrow(() -> new CustomException(FamilyErrorCode.FAMILY_NOT_FOUND));
  }

  private void validateSameFamily(User user, Family family) {
    if (!user.getFamily().getId().equals(family.getId())) {
      throw new CustomException(FamilyErrorCode.NOT_SAME_FAMILY);
    }
  }
}
