package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.model.User;
import com.wewe.marketflow.repository.UserRepository;
import com.wewe.marketflow.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        // เช็คก่อนว่า email นี้มีอยู่แล้วหรือไม่ (ยกเว้นกรณีเป็นการ update ตัวเอง)
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            if (user.getId() == null || !existing.getId().equals(user.getId())) {
                throw new IllegalArgumentException("อีเมลนี้ถูกใช้งานแล้ว");
            }
        });

        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }


    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = getById(userId);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
