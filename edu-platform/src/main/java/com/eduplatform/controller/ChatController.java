package com.eduplatform.controller;

import com.eduplatform.model.ChatRoom;
import com.eduplatform.model.Message;
import com.eduplatform.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/rooms/course/{courseId}")
    public ResponseEntity<?> createCourseChatRoom(@PathVariable String courseId) {
        try {
            ChatRoom chatRoom = chatService.createCourseChatRoom(courseId);
            return ResponseEntity.ok(chatRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/rooms/course/{courseId}")
    public ResponseEntity<?> getCourseChatRoom(@PathVariable String courseId) {
        ChatRoom chatRoom = chatService.getChatRoomByCourse(courseId);
        if (chatRoom == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getChatHistory(@PathVariable String roomId) {
        try {
            List<Message> messages = chatService.getChatHistory(roomId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable String roomId, @RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String content = request.get("content");

            if (userId == null || content == null) {
                return ResponseEntity.badRequest().body("User ID and message content are required");
            }

            Message message = chatService.sendMessage(roomId, userId, content);

            // 使用WebSocket向聊天室推送消息
            messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);

            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String messageId, @RequestParam String userId) {
        try {
            chatService.markMessageAsRead(messageId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}