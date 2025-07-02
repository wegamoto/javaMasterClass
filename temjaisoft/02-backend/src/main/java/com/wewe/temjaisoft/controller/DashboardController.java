package com.wewe.temjaisoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        model.addAttribute("username", principal != null ? principal.getName() : "Guest");
        return "dashboard"; // templates/dashboard.html (หน้าหลักระบบ)
    }
}
