package com.eduplatform.model;

import lombok.Data;

@Data
public class UserPreference {
    private String userId;
    private boolean emailNotification = true;
    private boolean smsNotification = false;
    private boolean assignmentReminder = true;
    private boolean courseUpdateNotification = true;
    private boolean messageNotification = true;
    private String quietHoursStart = "22:00";
    private String quietHoursEnd = "07:00";
    private boolean quietHoursEnabled = false;
    private NotificationFrequency notificationFrequency = NotificationFrequency.DAILY;

    public enum NotificationFrequency {
        IMMEDIATE, HOURLY, DAILY, WEEKLY
    }
}