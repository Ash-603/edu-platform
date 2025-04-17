package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public class Notification {
    private String notificationId;
    private String userId;
    private String title;
    private String content;
    private NotificationType type;
    private Date createdAt;
    private boolean read;

    public enum NotificationType {
        ASSIGNMENT, COURSE_UPDATE, MESSAGE, SYSTEM
    }
}