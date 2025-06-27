package com.wewe.eduexam.controller;

import com.wewe.eduexam.dto.RegisterRequest;
import com.wewe.eduexam.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;  // สมมติคุณมี Service สำหรับ register user

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // ตรงกับ templates/register.html
    }

//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("registerRequest", new RegisterRequest());
//        return "register"; // ชื่อไฟล์ HTML ที่จะไป render
//    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegisterRequest registerRequest, Model model) {
        try {
            userService.register(registerRequest);
            return "redirect:/login";  // สมัครเสร็จไปหน้า login
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}

