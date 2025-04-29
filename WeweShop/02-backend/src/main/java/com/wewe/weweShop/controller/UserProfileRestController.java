package com.wewe.weweShop.controller;

import com.wewe.weweShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class UserProfileRestController {

    @Autowired
    private UserService userService;

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(Principal principal) {
        userService.deleteUserByEmail(principal);
        return ResponseEntity.ok().build();
    }
}

