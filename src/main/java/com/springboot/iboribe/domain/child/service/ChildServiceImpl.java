package com.springboot.iboribe.domain.child.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.aisummary.repository.AiSummaryRepository;
import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.child.dto.request.ChildUpdateRequest;
import com.springboot.iboribe.domain.child.dto.response.ChildResponse;
import com.springboot.iboribe.domain.child.dto.response.ChildUpdateResponse;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.exception.ChildErrorCode;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.domain.notification.repository.NotificationRepository;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildServiceImpl implements ChildService {

  private final UserRepository userRepository;
  private final ChildRepository childRepository;
  private final NotificationRepository notificationRepository;
  private final AiSummaryRepository aiSummaryRepository;
  private final MedicalRecordRepository medicalRecordRepository;

  @Override
  public List<ChildResponse> getChildren(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    Family family = user.getFamily();

    return childRepository.findAllByFamily(family).stream().map(ChildResponse::from).toList();
  }

  @Override
  @Transactional
  public ChildUpdateResponse updateChild(Long childId, ChildUpdateRequest request) {
    Child child =
        childRepository
            .findById(childId)
            .orElseThrow(() -> new CustomException(ChildErrorCode.CHILD_NOT_FOUND));

    child.update(
        request.getProfileColor(),
        request.getBirthDate(),
        request.getNickname(),
        request.getHeight(),
        request.getWeight(),
        request.getMemo());

    return ChildUpdateResponse.from(child);
  }

  @Override
  @Transactional
  public void deleteChild(Long loginUserId, Long childId) {
    User loginUser =
        userRepository
            .findById(loginUserId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    Child child =
        childRepository
            .findById(childId)
            .orElseThrow(() -> new CustomException(ChildErrorCode.CHILD_NOT_FOUND));

    if (!child.getFamily().getId().equals(loginUser.getFamily().getId())) {
      throw new CustomException(ChildErrorCode.CHILD_NOT_FOUND);
    }

    notificationRepository.deleteAllByChildId(childId);
    aiSummaryRepository.deleteAllByMedicalRecordChildId(childId);
    medicalRecordRepository.deleteAllByChildId(childId);
    childRepository.delete(child);
  }
}
