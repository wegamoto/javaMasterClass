package com.wewe.proflow.controller.web;

import com.wewe.proflow.dto.ProjectDTO;
import com.wewe.proflow.model.Project;
import com.wewe.proflow.repository.ProjectRepository;
import com.wewe.proflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectWebController {

    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectWebController(ProjectService projectService, ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    // ✅ แก้ไขให้โหลด projects ทั้งหมดมาใช้ใน select.html
    @GetMapping("/select")
    public String showProjectSelector(Model model) {
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        return "projects/select";
    }

    // ✅ ตัวกลาง: รับ projectId จากฟอร์ม แล้ว redirect ไป /projects/{id}
    @GetMapping("/go")
    public String redirectToProjectDetail(@RequestParam("projectId") Long projectId) {
        return "redirect:/projects/" + projectId;
    }

    // ✅ แสดง projectphase.html (รายละเอียดโปรเจกต์ + phases)
    @GetMapping("/{id}")
    public String viewProjectDetail(@PathVariable Long id, Model model) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        model.addAttribute("project", project);
        return "projects/projectphase";
    }

    // Utility method ตรวจสอบ ownerId ถ้า null redirect ไปหน้าเลือก owner หรือ error page
    private boolean checkOwnerId(Long ownerId) {
        return ownerId != null && ownerId > 0;
    }

    // สร้าง redirect URL แบบปลอดภัย มี ownerId แน่นอน
    private String buildRedirectToList(Long ownerId) {
        if (!checkOwnerId(ownerId)) {
            // กรณี ownerId ไม่ถูกต้อง เปลี่ยน redirect ตาม logic ของคุณเอง
            return "redirect:/owners/select"; // ตัวอย่างเปลี่ยนเป็นหน้าเลือก owner
        }
        return "redirect:/projects?ownerId=" + ownerId;
    }

    // List projects by ownerId with pagination
    @GetMapping
    public String listProjects(
            Model model,
            @RequestParam(value = "ownerId", required = false) Long ownerId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        if (!checkOwnerId(ownerId)) {
            return "redirect:/owners/select";
        }

        List<ProjectDTO> projects = projectService.getProjectsByOwnerPaged(ownerId, page, size);
        long totalProjects = projectService.countProjectsByOwner(ownerId);
        long totalPages = (totalProjects + size - 1) / size;

        model.addAttribute("projects", projects);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);

        return "projects/list";
    }

    // Show form for creating a new project
    @GetMapping("/create")
    public String createForm(Model model, @RequestParam(value = "ownerId", required = false) Long ownerId) {
        if (!checkOwnerId(ownerId)) {
            return "redirect:/owners/select";
        }

        ProjectDTO dto = new ProjectDTO();
        dto.setOwnerId(ownerId);
        model.addAttribute("project", dto);
        return "projects/create";
    }

    // Save new project
    @PostMapping
    public String save(
            @Valid @ModelAttribute("project") ProjectDTO dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (!checkOwnerId(dto.getOwnerId())) {
            model.addAttribute("error", "Owner ID is required.");
            return "projects/form";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("ownerId", dto.getOwnerId());
            return "projects/form";
        }

        projectService.createProject(dto);
        return buildRedirectToList(dto.getOwnerId());
    }

    // Show an edit form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProjectDTO dto = projectService.getProjectById(id);
        if (dto == null || !checkOwnerId(dto.getOwnerId())) {
            return "redirect:/owners/select";
        }
        model.addAttribute("project", dto);
        return "projects/form";
    }

    // Update project
    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("project") ProjectDTO dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (!checkOwnerId(dto.getOwnerId())) {
            model.addAttribute("error", "Owner ID is required.");
            return "projects/form";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("ownerId", dto.getOwnerId());
            return "projects/form";
        }

        dto.setId(id);
        projectService.updateProject(id, dto);
        return buildRedirectToList(dto.getOwnerId());
    }

    // Delete project
    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam(value = "ownerId", required = false) Long ownerId
    ) {
        if (!checkOwnerId(ownerId)) {
            return "redirect:/owners/select";
        }
        projectService.deleteProject(id);
        return buildRedirectToList(ownerId);
    }

//    // ✅ แสดง projectphase.html (รายละเอียดโปรเจกต์ + phases)
//    @GetMapping("/{id}")
//    public String viewProjectDetail(@PathVariable Long id, Model model) {
//        Project project = projectRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
//
//        model.addAttribute("project", project);
//        return "projects/projectphase"; // ชี้ไปที่ projectphase.html
//    }
}
