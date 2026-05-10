package com.springboot.iboribe.domain.notification.service;

import java.util.List;

import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.notification.dto.response.NotificationResponse;
import com.springboot.iboribe.domain.notification.dto.response.NotificationStatusResponse;

public interface NotificationService {

  /** AI 요약 생성 시 같은 가족 구성원(본인 제외)에게 알림 생성 */
  void createFamilyNotifications(AiSummary aiSummary, Long requesterId);

  /** 내 알림 목록 조회 (최신순) */
  List<NotificationResponse> getMyNotifications(Long userId);

  /** 전체 알림 읽음 처리 */
  void markAllAsRead(Long userId);

  /** 읽지 않은 알림 존재 여부 및 개수 조회 */
  NotificationStatusResponse getUnreadStatus(Long userId);
}
