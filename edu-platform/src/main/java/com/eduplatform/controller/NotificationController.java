package com.eduplatform.controller;

import com.eduplatform.model.Notification;
import com.eduplatform.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{userId}/unread")
    public ResponseEntity<?> getUnreadNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            String title = (String) request.get("title");
            String content = (String) request.get("content");
            Notification.NotificationType type = Notification.NotificationType.valueOf(
                    ((String) request.get("type")).toUpperCase()
            );

            if (userId == null || title == null || content == null || type == null) {
                return ResponseEntity.badRequest().body("User ID, title, content, and type are required");
            }

            notificationService.sendNotification(userId, title, content, type);

            // 使用WebSocket实时推送通知
            messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", Map.of(
                    "title", title,
                    "content", content,
                    "type", type
            ));

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable String userId) {
        try {
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}