package com.wewe.springlance.controller;

import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import com.wewe.springlance.repository.ProjectRequestRepository;
import com.wewe.springlance.repository.UserRepository;
import com.wewe.springlance.service.ProjectRequestService;
import com.wewe.springlance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
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

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectRequestRepository.findAllByOrderByCreatedAtDesc());
        return "project/list";
    }

    @GetMapping("/my")
    public String myProjects(Model model, Principal principal) {
        String email = principal.getName(); // get email
        Optional<User> user = userService.findByEmail(email);
        List<ProjectRequest> myProjects = projectRequestService.findByClient(user); // เปลี่ยนเป็น client แทน owner ตาม entity ที่มี
        model.addAttribute("myProjects", myProjects);
        return "project/my";
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new ProjectRequest());
        return "project/create"; // /templates/project/create.html
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute ProjectRequest projectRequest) {
        projectRequest.setStatus("REQUESTED");
        projectRequest.setCreatedAt(java.time.LocalDateTime.now());
        projectRequestService.save(projectRequest);
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        projectRequestService.findById(id).ifPresent(project ->
                model.addAttribute("project", project)
        );
        return "project/view"; // /templates/project/view.html
    }
}

