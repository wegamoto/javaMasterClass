package com.wewe.temjaisoft.controller;

import com.wewe.temjaisoft.model.Department;
import com.wewe.temjaisoft.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hr/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public String listDepartments(Model model, Principal principal) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        return "hr/department-list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model, Principal principal) {
        model.addAttribute("department", new Department());
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        return "hr/department-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Department department, Principal principal) {
        departmentRepository.save(department);
        return "redirect:/hr/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
        model.addAttribute("department", department);
        return "hr/department-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, Principal principal) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department ID: " + id));

        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "ไม่สามารถลบแผนกได้ เนื่องจากยังมีพนักงานอยู่ในแผนกนี้");
            return "redirect:/hr/departments";
        }

        departmentRepository.delete(department);
        redirectAttributes.addFlashAttribute("success", "ลบแผนกเรียบร้อยแล้ว");
        return "redirect:/hr/departments";
    }
}

