package com.wewe.proflow.controller.web;

import com.wewe.proflow.dto.UserDTO;
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
}
