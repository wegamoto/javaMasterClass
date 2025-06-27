package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @PostMapping("/profile")
//    public String updateProfile(@ModelAttribute User user, Principal principal) {
//
//        // ตรวจสอบว่า principal ไม่เป็น null
//        if (principal == null) {
//            return "redirect:/login"; // หาก principal เป็น null (ไม่ได้ล็อกอิน) ให้รีไดเรกต์ไปที่หน้า login
//        }
//
//        String userEmail = null;
//
//        // รองรับทั้งแบบ login ธรรมดา และ OAuth2 (Facebook, Google, etc.)
//        if (principal instanceof OAuth2AuthenticationToken) {
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
//            OAuth2User userDetails = oauthToken.getPrincipal();
//            userEmail = (String) userDetails.getAttributes().get("email");
//        } else {
//            // ดึง email ของผู้ใช้ที่ล็อกอินอยู่ แบบ local
//            userEmail = principal.getName();
//        }
//
//        if (userEmail == null) {
//            return "redirect:/login"; // fallback เผื่อดึง email ไม่ได้
//        }
//
//        // กำหนด email ของผู้ใช้ให้ตรงกับ email ที่ล็อกอินอยู่ (เพื่อป้องกันการแก้ไข email)
//        user.setEmail(userEmail);
//
//        // อัพเดทข้อมูลโปรไฟล์
//        userService.updateProfile(userEmail, user);
//
//        // Redirect กลับไปที่หน้า profile และแสดงข้อความ success
//        return "redirect:/profile?success";
//    }
}

