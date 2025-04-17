package com.eduplatform.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SecurityUtils {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean validatePassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }

    public static String sanitizeInput(String input) {
        // 简单实现，实际应该使用更复杂的方法防止XSS等
        if (input == null) {
            return null;
        }
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}