package com.wewe.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // คืนค่าไปยัง login.html (ใน templates)
    }

//    @GetMapping("/")
//    public String homePage() {
//        return "redirect:/dashboard"; // หรือหน้าแรกของระบบ
//    }
}

