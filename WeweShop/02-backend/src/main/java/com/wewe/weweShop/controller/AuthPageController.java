package com.wewe.weweShop.controller;

import com.wewe.weweShop.dto.AuthRequest;
import com.wewe.weweShop.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthPageController {

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "login";
    }

    @GetMapping("/login-form") // 👈 แสดงหน้า login ที่ไม่ซ้ำกับ POST /login
    public String showLoginForm() {
        return "login"; // login.html ใน templates
    }
}

