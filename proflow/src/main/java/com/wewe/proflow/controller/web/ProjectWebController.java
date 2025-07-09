package com.wewe.proflow.controller.web;

import com.wewe.proflow.dto.OwnerDTO;
import com.wewe.proflow.dto.ProjectDTO;
import com.wewe.proflow.dto.UserDTO;
import com.wewe.proflow.model.Owner;
import com.wewe.proflow.model.Project;
import com.wewe.proflow.repository.ProjectRepository;
import com.wewe.proflow.service.OwnerService;
import com.wewe.proflow.service.ProjectService;
import com.wewe.proflow.service.UserService;
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

    private final OwnerService ownerService;

    // ✅ เพิ่ม UserService
    private final UserService userService;

    @Autowired
    public ProjectWebController(ProjectService projectService, ProjectRepository projectRepository, OwnerService ownerService, UserService userService) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.ownerService = ownerService;
        this.userService = userService;
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

    // ✅ Show form for creating a new project (เพิ่ม owners ใน model)
    @GetMapping("/create")
    public String createForm(Model model, @RequestParam(value = "ownerId", required = false) Long ownerId) {
        if (!checkOwnerId(ownerId)) {
            return "redirect:/owners/select";
        }

        ProjectDTO dto = new ProjectDTO();
        dto.setOwnerId(ownerId);
        model.addAttribute("project", dto);

        // 🔹 เพิ่มรายชื่อ owner สำหรับ dropdown
        List<Owner> owners = ownerService.getAllOwners();
        model.addAttribute("owners", owners);

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

    // ✅ Show edit form (พร้อมรองรับ ownerId + owners สำหรับ dropdown)
    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            @RequestParam(value = "ownerId", required = false) Long ownerId,
            Model model
    ) {
        ProjectDTO dto = projectService.getProjectById(id);

        if (dto == null) {
            return "redirect:/owners/select";
        }

        // ✅ ถ้ามี ownerId จาก query ให้ override เพื่อป้องกัน inconsistency
        if (ownerId != null && ownerId > 0) {
            dto.setOwnerId(ownerId);
        }

        // ✅ ตรวจสอบ ownerId อีกครั้งเพื่อความปลอดภัย
        if (!checkOwnerId(dto.getOwnerId())) {
            return "redirect:/owners/select";
        }

        model.addAttribute("project", dto);

        // 🔹 เพิ่มรายชื่อ owner สำหรับ dropdown
        List<Owner> owners = ownerService.getAllOwners();
        model.addAttribute("owners", owners);


        return "projects/create"; // ใช้หน้าเดียวกับ create สำหรับการ edit
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
}
