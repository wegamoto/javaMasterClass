package com.servix.maintenance.controller.page;

import com.servix.maintenance.model.Role;
import com.servix.maintenance.model.User;
import com.servix.maintenance.repository.RoleRepository;
import com.servix.maintenance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/technicians")
public class TechnicianFormController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // เพิ่ม method สำหรับแสดงฟอร์ม เพิ่ม technician
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "technicians/technicians-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found with id: " + id));
        model.addAttribute("user", user);
        return "technicians/technicians-form";  // ชื่อไฟล์เทมเพลตสำหรับฟอร์ม
    }


    @PostMapping("/save-form")
    public String saveTechnician(@ModelAttribute("user") User user) {
        // ค้นหา ROLE_TECHNICIAN ถ้าไม่มีให้ throw exception
        Role techRole = roleRepository.findByName("ROLE_TECHNICIAN")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_TECHNICIAN' not found"));

        // ตั้งค่า Role และรหัสผ่าน
        user.setRoles(Set.of(techRole));

        // หากรหัสผ่านยังไม่ถูกเข้ารหัส ให้กำหนดค่า default
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode("default123"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // บันทึกผู้ใช้
        userRepository.save(user);

        // กลับไปที่หน้ารายชื่อช่าง
        return "redirect:/technicians";
    }

}

