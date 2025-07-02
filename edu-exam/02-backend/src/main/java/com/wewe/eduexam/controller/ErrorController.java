package com.wewe.eduexam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // ชื่อไฟล์ template เช่น access-denied.html
    }
}

