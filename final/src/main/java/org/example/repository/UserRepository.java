package org.example.repository;

import org.example.Tables.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
    boolean deleteUser(long id);
    List<User> getAllUsers();
}