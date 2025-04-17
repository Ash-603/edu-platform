package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public class Message {
    private String messageId;
    private String sender;
    private String content;
    private Date sentAt;
    private boolean read;
}