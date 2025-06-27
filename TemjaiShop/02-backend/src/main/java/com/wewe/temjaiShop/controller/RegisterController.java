package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.dto.RegisterRequest;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class RegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // แสดงหน้า register
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest()); // ✅ ใส่ object ที่ตรงกับ form
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "register"; // Validation ไม่ผ่าน
        }

        // ตรวจซ้ำว่าชื่อผู้ใช้ซ้ำหรือไม่
        if (userService.existsByUsername(registerRequest.getUsername())) {
            model.addAttribute("error", "ชื่อผู้ใช้นี้ถูกใช้แล้ว");
            return "register";
        }

        // ตรวจซ้ำว่าอีเมลซ้ำหรือไม่ (ถ้ามี)
        if (userService.existsByEmail(registerRequest.getEmail())) {
            model.addAttribute("error", "อีเมลนี้ถูกใช้แล้ว");
            return "register";
        }

        // สร้างผู้ใช้ใหม่
        // ✅ ไม่ต้อง setRoles ที่นี่ ให้ Service จัดการ
        User user = new User();
        user.setUsername(registerRequest.getUsername()); // ❗ เดิมคุณ set จาก email ซึ่งผิด
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//        user.setRoles(Collections.singletonList("ROLE_USER")); // ✅ เพิ่ม role เริ่มต้น

        userService.save(user);  // กำหนสด ROLE_USER ให้ใน Service

        return "redirect:/login?registerSuccess";
    }

////    // รับข้อมูลจากแบบฟอร์มและบันทึกผู้ใช้ เข้ารหัสรหัสผ่านก่อนบันทึก
//    @PostMapping("/register")
//    public String processRegistration(@ModelAttribute("user") User user, Model model) {
//        if (userService.existsByUsername(user.getUsername())) {
//            model.addAttribute("error", "ชื่อผู้ใช้นี้ถูกใช้แล้ว");
//            return "register";
//        }
//
//        // เข้ารหัสรหัสผ่านก่อนบันทึก
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userService.save(user);
//
//        return "redirect:/login?registerSuccess";
//    }
}

