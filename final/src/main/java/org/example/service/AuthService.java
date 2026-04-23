package org.example.service;

import org.example.Tables.Role;
import org.example.Tables.User;
import org.example.repository.StudentRepository;
import org.example.repository.StudentRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private User currentUser;
    private static final StudentRepository repository = new StudentRepositoryImpl();

    public boolean register(String username, String password) {
        if (username.isBlank()) {
            System.out.println("Username cannot be empty");
            return false;
        }
        else if (password.isBlank() || password.length() < 7) {
            System.out.println("Password cannot be empty or cannot be shorter than 7");
            return false;
        }
        User user = new User(username, hashPassword(password));
        return repository.register(user);
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
        User user = repository.login(username, password);
        if (username.equals("student") && password.equals("student")) {
            currentUser = new User("student");
            return true;
        }
        else if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            currentUser = user;
            currentUser.setRole(Role.TEACHER);
            return true;
        }
        System.out.println(user);
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isTeacher() {
        return currentUser != null && currentUser.getRole() == Role.TEACHER;
    }

    public boolean isStudent() {
        return currentUser != null && currentUser.getRole() == Role.STUDENT;
    }
}