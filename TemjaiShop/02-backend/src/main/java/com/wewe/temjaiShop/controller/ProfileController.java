package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String email = null;

        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            OAuth2User userDetails = oauthToken.getPrincipal();

            // ดึง email จาก Facebook หรือ provider อื่น
            email = (String) userDetails.getAttributes().get("email");

            System.out.println("Facebook/Google Email: " + email);
        } else {
            // สำหรับ login แบบ local
            email = principal.getName();
            System.out.println("Local Email: " + email);
        }

        // ดึงข้อมูลผู้ใช้จาก repository หรือ service
        User user = userService.getUserByUsernameOrEmail(email);

        // ส่งข้อมูลไปยังหน้า Profile
        model.addAttribute("user", user);
        return "profile"; // ส่งไปที่หน้า profile.html
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail;

        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User userDetails = oauthToken.getPrincipal();
            userEmail = (String) userDetails.getAttributes().get("email");

            // บางกรณี Facebook ไม่ส่ง email ต้อง fallback ด้วย id หรือ name
            if (userEmail == null) {
                userEmail = (String) userDetails.getAttributes().get("name");
            }
        } else {
            userEmail = principal.getName();
        }

        if (userEmail == null || userEmail.isEmpty()) {
            return "redirect:/login"; // fallback ป้องกัน null หรือว่าง
        }

        // ป้องกันการแก้ไข email จาก form โดย override ชัดเจน
        user.setEmail(userEmail);

        // อัพเดทเฉพาะ field ที่อนุญาต
        userService.updateProfile(userEmail, user);

        return "redirect:/profile?success";
    }
}

