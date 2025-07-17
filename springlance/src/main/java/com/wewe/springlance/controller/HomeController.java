package com.wewe.springlance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Welcome to Springlance");
        return "index"; // /templates/index.html
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // TODO: load user-specific data
        model.addAttribute("dashboardMessage", "Welcome to your dashboard");
        return "dashboard"; // /templates/dashboard.html
    }
}

