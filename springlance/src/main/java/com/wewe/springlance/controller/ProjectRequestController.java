package com.wewe.springlance.controller;

import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import com.wewe.springlance.repository.ProjectRequestRepository;
import com.wewe.springlance.service.ProjectRequestService;
import com.wewe.springlance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
public class ProjectRequestController {

    @Autowired
    private ProjectRequestService projectRequestService;

    @Autowired
    private ProjectRequestRepository projectRequestRepository;

    @Autowired
    private UserService userService;

    // 📄 1. แสดงรายการโปรเจกต์ทั้งหมด
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectRequestRepository.findAllByOrderByCreatedAtDesc());
        return "project/list";
    }


    // 👤 2. แสดงเฉพาะโปรเจกต์ของผู้ใช้ปัจจุบัน
    @GetMapping("/my")
    public String myProjects(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userService.findByEmail(email);
        List<ProjectRequest> myProjects = projectRequestService.findByClient(user);
        model.addAttribute("myProjects", myProjects);
        return "project/my";
    }

    // 🆕 3. ฟอร์มสร้างโปรเจกต์ใหม่
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("projectRequest", new ProjectRequest());
        return "project/create-project";
    }

    // 💾 4. บันทึกโปรเจกต์ใหม่
    @PostMapping
    public String saveProject(@ModelAttribute ProjectRequest projectRequest, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userService.findByEmail(email);
        user.ifPresent(projectRequest::setClient); // ตั้ง client จาก user ที่ล็อกอิน
        projectRequest.setStatus("REQUESTED");
        projectRequest.setCreatedAt(LocalDateTime.now());
        projectRequestService.save(projectRequest);
        return "redirect:/projects/my";
    }

    // 🔍 5. ดูโปรเจกต์ตาม id
    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        projectRequestService.findById(id).ifPresent(project ->
                model.addAttribute("project", project)
        );
        return "project/view";
    }
}
