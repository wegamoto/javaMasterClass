package com.wewe.marketflow.service;

import com.wewe.marketflow.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    User save(User user);
    User getById(Long id);
    void delete(Long id);
    void changePassword(Long userId, String newPassword);
    Optional<User> findByEmail(String email);  // เพิ่มตรงนี้
}

