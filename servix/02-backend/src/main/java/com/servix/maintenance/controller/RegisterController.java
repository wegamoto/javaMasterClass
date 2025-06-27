package com.servix.maintenance.controller;

import com.servix.maintenance.config.SecurityConfig;
import com.servix.maintenance.model.Role;
import com.servix.maintenance.model.User;
import com.servix.maintenance.repository.RoleRepository;
import com.servix.maintenance.repository.UserRepository;
import groovy.lang.Lazy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Lazy
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // เพิ่มบรรทัดนี้ให้แน่ใจว่ามี user object ใน model
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            @RequestParam("role") String roleName,
            Model model) {

        // ตรวจสอบ validation errors
        if (bindingResult.hasErrors()) {
            // ส่ง user กลับไปที่ฟอร์มพร้อม error
            return "register";
        }

        // ตรวจสอบ username ซ้ำ
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        // หา Role จาก roleName หรือโยน exception ถ้าไม่เจอ
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // ตั้งค่า Role, เข้ารหัสรหัสผ่าน, เปิดใช้งาน user
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // บันทึก user ลงฐานข้อมูล
        userRepository.save(user);

        // รีไดเร็คไปหน้า login พร้อมพารามิเตอร์แสดงข้อความสำเร็จ
        return "redirect:/login?registerSuccess";
    }

}
