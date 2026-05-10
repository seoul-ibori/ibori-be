package com.springboot.iboribe.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "child_id", nullable = false)
  private Child child;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ai_summary_id", nullable = false)
  private AiSummary aiSummary;

  @Builder.Default
  @Column(name = "is_read", nullable = false)
  private boolean read = false;

  public void markAsRead() {
    this.read = true;
  }
}
