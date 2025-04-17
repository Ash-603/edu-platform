package com.eduplatform.service;

import com.eduplatform.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NotificationService {
    private Map<String, List<Notification>> userNotifications = new HashMap<>();
    private final UserService userService;

    public NotificationService(UserService userService) {
        this.userService = userService;
    }

    public void sendNotification(String userId, String title, String content, Notification.NotificationType type) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Notification notification = new Notification();
        notification.setNotificationId(generateNotificationId());
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setCreatedAt(new Date());
        notification.setRead(false);

        if (!userNotifications.containsKey(userId)) {
            userNotifications.put(userId, new ArrayList<>());
        }

        userNotifications.get(userId).add(notification);

        // 实际实现可能还需要通过WebSocket推送通知
        System.out.println("Notification sent to user: " + userId);
    }

    public void sendPaymentSuccessNotification(String studentId, String courseId) {
        String title = "Payment Successful";
        String content = "Your payment for course has been processed successfully. You can now access the course materials.";
        sendNotification(studentId, title, content, Notification.NotificationType.SYSTEM);
    }

    public void sendGradingNotification(String studentId, String assignmentId, int score) {
        String title = "Assignment Graded";
        String content = "Your assignment has been graded. You received a score of " + score + ".";
        sendNotification(studentId, title, content, Notification.NotificationType.ASSIGNMENT);
    }

    public void sendCourseUpdateNotification(String courseId, String updateContent) {
        // 向所有选修该课程的学生发送通知
        // 这里简化实现，实际中需要从数据库查询选修该课程的学生
        String title = "Course Update";
        String content = updateContent;

        // 模拟给所有学生发送通知
        System.out.println("Sending course update notification to all students enrolled in course: " + courseId);
    }

    public List<Notification> getUserNotifications(String userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>());
    }

    public List<Notification> getUnreadNotifications(String userId) {
        List<Notification> unread = new ArrayList<>();
        List<Notification> notifications = userNotifications.getOrDefault(userId, new ArrayList<>());

        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                unread.add(notification);
            }
        }

        return unread;
    }

    public void markAsRead(String notificationId) {
        for (List<Notification> notifications : userNotifications.values()) {
            for (Notification notification : notifications) {
                if (notification.getNotificationId().equals(notificationId)) {
                    notification.setRead(true);
                    return;
                }
            }
        }
    }

    public void markAllAsRead(String userId) {
        List<Notification> notifications = userNotifications.getOrDefault(userId, new ArrayList<>());
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
    }

    private String generateNotificationId() {
        return "notif_" + System.currentTimeMillis();
    }
}