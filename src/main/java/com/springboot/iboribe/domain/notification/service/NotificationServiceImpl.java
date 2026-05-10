package com.springboot.iboribe.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.notification.dto.response.NotificationResponse;
import com.springboot.iboribe.domain.notification.dto.response.NotificationStatusResponse;
import com.springboot.iboribe.domain.notification.entity.Notification;
import com.springboot.iboribe.domain.notification.repository.NotificationRepository;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void createFamilyNotifications(AiSummary aiSummary, Long requesterId) {
    Child child = aiSummary.getMedicalRecord().getChild();
    Family family = child.getFamily();
    List<User> allMembers = userRepository.findAllByFamily(family);

    User sender =
        allMembers.stream()
            .filter(user -> user.getId().equals(requesterId))
            .findFirst()
            .orElseThrow(
                () ->
                    new CustomException(
                        com.springboot.iboribe.domain.auth.exception.AuthErrorCode.USER_NOT_FOUND));

    allMembers.stream()
        .filter(user -> !user.getId().equals(requesterId))
        .forEach(
            receiver -> {
              Notification notification =
                  Notification.builder()
                      .receiver(receiver)
                      .sender(sender)
                      .child(child)
                      .aiSummary(aiSummary)
                      .build();
              notificationRepository.save(notification);
              log.info(
                  "[Notification] 알림 생성 - receiverId: {}, senderId: {}, childId: {}",
                  receiver.getId(),
                  sender.getId(),
                  child.getId());
            });
  }

  @Override
  public List<NotificationResponse> getMyNotifications(Long userId) {
    return notificationRepository.findAllByReceiverIdWithDetails(userId).stream()
        .map(NotificationResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public void markAllAsRead(Long userId) {
    notificationRepository.markAllAsReadByReceiverId(userId);
  }

  @Override
  public NotificationStatusResponse getUnreadStatus(Long userId) {
    long unreadCount = notificationRepository.countByReceiverIdAndReadFalse(userId);
    return NotificationStatusResponse.of(unreadCount);
  }
}
