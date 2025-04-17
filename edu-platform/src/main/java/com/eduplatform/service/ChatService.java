package com.eduplatform.service;

import com.eduplatform.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatService {
    private Map<String, ChatRoom> chatRooms = new HashMap<>();
    private final UserService userService;

    public ChatService(UserService userService) {
        this.userService = userService;
    }

    public ChatRoom createCourseChatRoom(String courseId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(generateRoomId());
        chatRoom.setCourseId(courseId);

        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public ChatRoom getChatRoomByCourse(String courseId) {
        for (ChatRoom room : chatRooms.values()) {
            if (room.getCourseId().equals(courseId)) {
                return room;
            }
        }
        return null;
    }

    public Message sendMessage(String roomId, String userId, String content) {
        ChatRoom chatRoom = chatRooms.get(roomId);
        if (chatRoom == null) {
            throw new RuntimeException("Chat room not found");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Message message = new Message();
        message.setMessageId(generateMessageId());
        message.setSender(userId);
        message.setContent(content);
        message.setSentAt(new Date());
        message.setRead(false);

        chatRoom.getMessages().add(message);

        return message;
    }

    public List<Message> getChatHistory(String roomId) {
        ChatRoom chatRoom = chatRooms.get(roomId);
        if (chatRoom == null) {
            throw new RuntimeException("Chat room not found");
        }
        return chatRoom.getMessages();
    }

    public void markMessageAsRead(String messageId, String userId) {
        for (ChatRoom room : chatRooms.values()) {
            for (Message message : room.getMessages()) {
                if (message.getMessageId().equals(messageId)) {
                    message.setRead(true);
                    return;
                }
            }
        }
    }

    private String generateRoomId() {
        return "room_" + System.currentTimeMillis();
    }

    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis();
    }
}