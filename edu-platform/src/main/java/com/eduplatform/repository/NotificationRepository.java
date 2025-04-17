package com.eduplatform.repository;

import com.eduplatform.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(String userId, boolean read);

    long countByUserIdAndRead(String userId, boolean read);

    List<Notification> findByUserIdAndType(String userId, Notification.NotificationType type);

    void deleteByUserIdAndCreatedAtBefore(String userId, java.util.Date date);
}