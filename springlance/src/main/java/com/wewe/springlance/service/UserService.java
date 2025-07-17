package com.wewe.springlance.service;

import com.wewe.springlance.model.User;

import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User findByUsername(String username);
}
