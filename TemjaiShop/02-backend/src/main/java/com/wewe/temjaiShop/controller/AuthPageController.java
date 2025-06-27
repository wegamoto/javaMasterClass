package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthPageController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String defaultHome() {
        return "home"; // ชี้ไปยัง home.html
    }

    @GetMapping("/home")
    public String home() {
        return "dashboard";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // -->view เป็น login.html
    }
}


