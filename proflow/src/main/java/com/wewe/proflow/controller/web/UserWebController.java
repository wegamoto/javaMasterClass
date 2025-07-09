package com.wewe.proflow.controller.web;

import com.wewe.proflow.dto.UserDTO;
import com.wewe.proflow.model.Role;
import com.wewe.proflow.repository.UserRepository;
import com.wewe.proflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list"; // Thymeleaf: templates/users/list.html
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/form"; // templates/users/form.html
    }

    // แสดงฟอร์มแก้ไข
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values()); // ✅ เพิ่มรายการ role ทั้งหมด
        return "users/edit"; // templates/users/edit.html
    }

    // บันทึกข้อมูลที่แก้ไขแล้ว
    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserDTO userDTO) {
        userService.updateUser(userDTO);
        return "redirect:/users";
    }

    @PostMapping
    public String save(@ModelAttribute UserDTO dto) {
        userService.createUser(dto);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    // Optional: ดูเฉพาะ contractor
    @GetMapping("/contractors")
    public String listContractors(Model model) {
        model.addAttribute("users", userRepository.findByRole(Role.CONTRACTOR));
        return "users/list";
    }

}
