package com.springboot.iboribe.domain.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationStatusResponse {

  @Schema(description = "읽지 않은 알림 존재 여부", example = "true")
  private boolean hasUnread;

  @Schema(description = "읽지 않은 알림 수", example = "3")
  private long unreadCount;

  public static NotificationStatusResponse of(long unreadCount) {
    return new NotificationStatusResponse(unreadCount > 0, unreadCount);
  }
}
