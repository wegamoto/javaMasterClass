package com.wewe.weweShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // -->view เป็น login.html
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // ชี้ไปยัง register.html (ถ้ามี)
    }
}


