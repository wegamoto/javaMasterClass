package com.wewe.autogate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";  // ไฟล์ login.html ที่อยู่ใน /templates
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/dashboard";
    }
}
