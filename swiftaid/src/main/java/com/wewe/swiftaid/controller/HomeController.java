package com.wewe.swiftaid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";  // ชื่อไฟล์ index.html ใน templates folder
    }
}
