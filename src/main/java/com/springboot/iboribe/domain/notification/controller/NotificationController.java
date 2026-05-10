package com.springboot.iboribe.domain.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.notification.dto.response.NotificationResponse;
import com.springboot.iboribe.domain.notification.dto.response.NotificationStatusResponse;
import com.springboot.iboribe.domain.notification.service.NotificationService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(
      summary = "[토큰 O] 내 알림 목록 조회",
      description =
          """
          **Returns**  \n
          id: 알림 ID  \n
          message: 알림 메시지 (예: 김길동 님께서 아이 우리막둥이의 진료 음성을 요약했습니다.)  \n
          timeAgo: 상대 시간 (예: 9시간 전)  \n
          read: 읽음 여부  \n
          """)
  @GetMapping
  public ResponseEntity<BaseResponse<List<NotificationResponse>>> getMyNotifications(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    List<NotificationResponse> response =
        notificationService.getMyNotifications(userDetails.getUserId());

    return ResponseEntity.ok(BaseResponse.success(200, "알림 목록 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 읽지 않은 알림 현황 조회",
      description =
          """
          알림 벨 아이콘의 읽지 않은 알림 여부 확인용  \n

          **Returns**  \n
          hasUnread: 읽지 않은 알림 존재 여부  \n
          unreadCount: 읽지 않은 알림 수  \n
          """)
  @GetMapping("/unread-status")
  public ResponseEntity<BaseResponse<NotificationStatusResponse>> getUnreadStatus(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    NotificationStatusResponse response =
        notificationService.getUnreadStatus(userDetails.getUserId());

    return ResponseEntity.ok(BaseResponse.success(200, "읽지 않은 알림 현황 조회 성공", response));
  }

  @Operation(summary = "[토큰 O] 전체 알림 읽음 처리", description = "알림 화면 진입 시 모든 알림을 읽음 상태로 처리")
  @PatchMapping("/read-all")
  public ResponseEntity<BaseResponse<Void>> markAllAsRead(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    notificationService.markAllAsRead(userDetails.getUserId());

    return ResponseEntity.ok(BaseResponse.success(200, "전체 알림 읽음 처리 성공", null));
  }
}
