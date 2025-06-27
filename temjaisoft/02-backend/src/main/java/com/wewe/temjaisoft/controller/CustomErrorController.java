package com.wewe.temjaisoft.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // return ชื่อไฟล์ thymeleaf template เช่น error.html
        return "error";
    }

    // สำหรับ Spring Boot 2.3+ ถ้าอยาก override path เอง ให้เพิ่ม method นี้
    /*
    @Override
    public String getErrorPath() {
        return "/error";
    }
    */
}

