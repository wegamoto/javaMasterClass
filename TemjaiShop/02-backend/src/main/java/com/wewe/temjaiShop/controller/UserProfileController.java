package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    // ดูโปรไฟล์ตัวเอง
    @GetMapping
    public ResponseEntity<User> getProfile(Principal principal) {
        User user = userService.getUserByEmail(principal);
        return ResponseEntity.ok(user);
    }

    // แก้ไขโปรไฟล์
    @PutMapping
    public ResponseEntity<User> updateProfile(Principal principal, @RequestBody User updatedUser) {
        String userEmail = principal.getName();
        User user = userService.updateProfile(userEmail, updatedUser);
        return ResponseEntity.ok(user);
    }
}

