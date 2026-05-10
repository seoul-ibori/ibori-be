package com.springboot.iboribe.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.iboribe.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query(
      "SELECT n FROM Notification n "
          + "JOIN FETCH n.sender "
          + "JOIN FETCH n.child "
          + "JOIN FETCH n.aiSummary "
          + "WHERE n.receiver.id = :receiverId "
          + "ORDER BY n.createTime DESC")
  List<Notification> findAllByReceiverIdWithDetails(@Param("receiverId") Long receiverId);

  long countByReceiverIdAndReadFalse(Long receiverId);

  @Modifying
  @Query(
      "UPDATE Notification n SET n.read = true WHERE n.receiver.id = :receiverId AND n.read = false")
  void markAllAsReadByReceiverId(@Param("receiverId") Long receiverId);
}
