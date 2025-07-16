package com.wewe.marketflow.controller.page;

import com.wewe.marketflow.model.User;
import com.wewe.marketflow.model.UserRole;
import com.wewe.marketflow.service.UserRoleService;
import com.wewe.marketflow.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserPageController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    public UserPageController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("")
    public String listUsers(Model model, Principal principal) {
        List<User> users = userService.getAll();

        // ตรวจสอบว่า principal ไม่เป็น null
        if (principal != null) {
            String email = principal.getName(); // สมมุติว่าใช้ email เป็น username
            Optional<User> currentUser = userService.findByEmail(email);
            currentUser.ifPresent(user -> model.addAttribute("currentUser", user));
        }

        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userRoleService.getAllRoles());
        return "user/create";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        List<UserRole> roles = userRoleService.getAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user/create";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/{id}/change-password")
    public String showChangePasswordForm(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "users/edit-password";
    }

    @PostMapping("/{id}/change-password")
    public String changePassword(@PathVariable Long id,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        userService.changePassword(id, newPassword);
        redirectAttributes.addFlashAttribute("success", "เปลี่ยนรหัสผ่านสำเร็จแล้ว");
        return "redirect:/users";
    }

    // หน้ารวมสำหรับ admin เปลี่ยนรหัสผ่านผู้ใช้
    @GetMapping("/change-password")
    public String passwordChangePage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users/change-password"; // แสดงหน้า user/change-password.html
    }

}

