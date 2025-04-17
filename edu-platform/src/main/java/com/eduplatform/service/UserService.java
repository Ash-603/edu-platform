package com.eduplatform.service;

import com.eduplatform.model.User;
import com.eduplatform.model.Student;
import com.eduplatform.model.Teacher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private Map<String, User> users = new HashMap<>();

    public User register(User user) {
        // 检查邮箱是否已被注册
        for (User existingUser : users.values()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new RuntimeException("Email already registered");
            }
        }

        String userId = generateUserId();
        user.setUserId(userId);
        users.put(userId, user);
        return user;
    }

    public User login(String email, String password) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                user.login();
                return user;
            }
        }
        return null; // 登录失败
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getUserId())) {
            throw new RuntimeException("User not found");
        }
        users.put(user.getUserId(), user);
        return user;
    }

    private String generateUserId() {
        return "user_" + System.currentTimeMillis();
    }
}