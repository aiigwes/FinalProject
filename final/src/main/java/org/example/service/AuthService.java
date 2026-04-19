package org.example.service;

import org.example.Tables.Role;
import org.example.Tables.User;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private Map<String, User> users = new HashMap<>();
    private User currentUser;

    public AuthService() {
        // Тестовые пользователи
        users.put("admin", new User("admin", hashPassword("admin"), Role.ADMIN));
        users.put("teacher", new User("teacher", hashPassword("teacher"), Role.TEACHER));
        users.put("student", new User("student", hashPassword("student"), Role.STUDENT));
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return password;
        }
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == Role.ADMIN;
    }

    public boolean isTeacher() {
        return currentUser != null && currentUser.getRole() == Role.TEACHER;
    }

    public boolean isStudent() {
        return currentUser != null && currentUser.getRole() == Role.STUDENT;
    }
}