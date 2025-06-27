package com.servix.maintenance.controller.page;

import com.servix.maintenance.model.Role;
import com.servix.maintenance.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping("/new")
    public String showCreateRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/role-form";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role) {
        roleRepository.save(role);
        return "redirect:/roles/list";
    }

    @GetMapping("/list")
    public String listRoles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "roles/role-list";
    }
}

