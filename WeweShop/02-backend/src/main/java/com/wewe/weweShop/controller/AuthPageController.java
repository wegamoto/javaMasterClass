package com.wewe.weweShop.controller;

import com.wewe.weweShop.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    private UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthPageController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String defaultHome() {
        return "home"; // ชี้ไปยัง home.html
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // -->view เป็น login.html
    }
}


