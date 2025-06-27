package com.wewe.temjaisoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        // ดึงชื่อผู้ใช้จาก Security context
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "Guest");
        }

        return "dashboard"; // คืนค่าไปยัง templates/dashboardOld.html
    }
}
