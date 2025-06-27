package com.servix.maintenance.controller.page;

import com.servix.maintenance.model.Role;
import com.servix.maintenance.model.SkillLevel;
import com.servix.maintenance.model.User;
import com.servix.maintenance.repository.RoleRepository;
import com.servix.maintenance.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/technicians")
public class TechnicianPageController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ แสดงรายชื่อช่างทั้งหมด
    @GetMapping
    public String listTechnicians(Model model) {
        List<User> technicians = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_TECHNICIAN")))
                .toList();
        model.addAttribute("technicians", technicians);
        return "technicians";
    }

//    // ✅ ฟอร์มเพิ่มช่างใหม่
//    @GetMapping("/new")
//    public String createTechnicianForm(Model model) {
//        model.addAttribute("technician", new User());
//        model.addAttribute("skillLevels", SkillLevel.values());
//        return "technicians/technicians-form";
//    }

    // ✅ บันทึกข้อมูลช่างใหม่
    @Transactional
    @PostMapping("/save")
    public String saveTechnician(@ModelAttribute("user") User user, Model model) {
        Role techRole = roleRepository.findByName("ROLE_TECHNICIAN")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_TECHNICIAN' not found"));

        user.setRoles(Set.of(techRole));

        if (user.getId() != null) {
            // Updating existing user
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ตรวจสอบว่าชื่อผู้ใช้ถูกเปลี่ยนและซ้ำหรือไม่
            if (!existingUser.getUsername().equals(user.getUsername())
                    && userRepository.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("errorMessage", "Username already exists.");
                model.addAttribute("user", user);
                return "technicians/technicians-form";
            }

            existingUser.setUsername(user.getUsername());
            existingUser.setFullName(user.getFullName());
            existingUser.setPhone(user.getPhone());
            existingUser.setEmail(user.getEmail());
            existingUser.setDepartment(user.getDepartment());
            existingUser.setSkillLevel(user.getSkillLevel());
            existingUser.setRoles(Set.of(techRole));
            existingUser.setEnabled(true);

            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userRepository.save(existingUser);

        } else {
            // Creating new user
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("errorMessage", "Username already exists.");
                model.addAttribute("user", user);
                return "technicians/technicians-form";
            }

            String rawPassword = user.getPassword();
            if (rawPassword == null || rawPassword.isBlank()) {
                rawPassword = "default123";
            }

            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setEnabled(true);

            userRepository.save(user);
        }

        return "redirect:/technicians";
    }



    // ✅ ฟอร์มแก้ไข
//    @GetMapping("/edit/{id}")
//    public String editTechnicianForm(@PathVariable Long id, Model model) {
//        User technician = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Technician not found"));
//        model.addAttribute("technician", technician);
//        model.addAttribute("skillLevels", SkillLevel.values());
//        return "technicians/technicians-form";
//    }

    // ✅ บันทึกการแก้ไข
    @PostMapping("/update/{id}")
    public String updateTechnician(@PathVariable Long id, @ModelAttribute("technician") User updated) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        existing.setUsername(updated.getUsername());
        existing.setFullName(updated.getFullName());
        userRepository.save(existing);
        return "redirect:/technicians";
    }

    // ✅ ลบ
    @PostMapping("/delete/{id}")
    public String deleteTechnician(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/technicians";
    }
}
