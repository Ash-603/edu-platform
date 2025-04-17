package com.eduplatform.model;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class ChatRoom {
    private String roomId;
    private String courseId;
    private List<Message> messages = new ArrayList<>();

    public void addMessage(User user, String content) {
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setContent(content);
        message.setSentAt(new Date());
        messages.add(message);
    }

    public List<Message> getMessageHistory() {
        return messages;
    }

    public void sendMessage(User sender, String content) {
        Message message = new Message();
        message.setSender(sender.getUserId());
        message.setContent(content);
        message.setSentAt(new Date());
        // 保存消息
        messages.add(message);
    }
}