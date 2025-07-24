package com.wewe.taxcertify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {
        return "login";  // => templates/login.html
    }

    // ✅ ไม่ต้องเขียน /logout เอง
}
