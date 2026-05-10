package com.springboot.iboribe.domain.notification.dto.response;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.notification.entity.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

  @Schema(description = "알림 ID", example = "1")
  private Long id;

  @Schema(description = "알림 메시지", example = "김길동 님께서 아이 우리막둥이의 진료 음성을 요약했습니다.")
  private String message;

  @Schema(description = "상대 시간", example = "9시간 전")
  private String timeAgo;

  @Schema(description = "읽음 여부", example = "false")
  private boolean read;

  public static NotificationResponse from(Notification notification) {
    String senderName = notification.getSender().getName();
    String childDisplayName = resolveChildDisplayName(notification.getChild());

    return NotificationResponse.builder()
        .id(notification.getId())
        .message(senderName + " 님께서 아이 " + childDisplayName + "의 진료 음성을 요약했습니다.")
        .timeAgo(formatTimeAgo(notification.getCreateTime()))
        .read(notification.isRead())
        .build();
  }

  private static String resolveChildDisplayName(Child child) {
    String nickname = child.getNickname();
    return (nickname != null && !nickname.isBlank()) ? nickname : child.getName();
  }

  private static String formatTimeAgo(LocalDateTime createTime) {
    long minutes = ChronoUnit.MINUTES.between(createTime, LocalDateTime.now());
    if (minutes < 1) return "방금 전";
    if (minutes < 60) return minutes + "분 전";
    long hours = minutes / 60;
    if (hours < 24) return hours + "시간 전";
    long days = hours / 24;
    if (days < 7) return days + "일 전";
    return (days / 7) + "주 전";
  }
}
