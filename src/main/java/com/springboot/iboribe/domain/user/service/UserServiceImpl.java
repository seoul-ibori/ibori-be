package com.springboot.iboribe.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.aisummary.repository.AiSummaryRepository;
import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.repository.FamilyRepository;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.domain.notification.repository.NotificationRepository;
import com.springboot.iboribe.domain.user.dto.response.UserInfoResponse;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final FamilyRepository familyRepository;
  private final NotificationRepository notificationRepository;
  private final ChildRepository childRepository;
  private final AiSummaryRepository aiSummaryRepository;
  private final MedicalRecordRepository medicalRecordRepository;

  @Override
  public UserInfoResponse getUserInfo(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    return UserInfoResponse.from(user);
  }

  @Override
  public List<UserInfoResponse> getAllUsers() {
    return userRepository.findAll().stream().map(UserInfoResponse::from).toList();
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    Family family = user.getFamily();
    notificationRepository.deleteAllByUserId(userId);
    userRepository.delete(user);

    boolean familyEmpty = userRepository.findAllByFamily(family).isEmpty();
    if (familyEmpty) {
      List<Child> children = childRepository.findAllByFamily(family);
      for (Child child : children) {
        notificationRepository.deleteAllByChildId(child.getId());
        aiSummaryRepository.deleteAllByMedicalRecordChildId(child.getId());
        medicalRecordRepository.deleteAllByChildId(child.getId());
      }
      childRepository.deleteAll(children);
      familyRepository.delete(family);
    }
  }
}
