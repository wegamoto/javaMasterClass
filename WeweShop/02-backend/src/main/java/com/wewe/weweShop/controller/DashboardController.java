package com.wewe.weweShop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", authentication.getName());
        return "dashboard"; // => src/main/resources/templates/dashboard.html
    }

//    @GetMapping("/dashboard")
//    public String showDashboard() {
//        return "dashboard";  // ชื่อไฟล์ dashboard.html ที่ควรจะมีอยู่ใน templates
//    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUsername = authentication.getName(); // ใช้ email หรือ username ขึ้นอยู่กับระบบ login
//        model.addAttribute("username", currentUsername);
//        return "dashboard";
//    }
}
