package com.wewe.proflow.controller.api;

import com.wewe.proflow.dto.ProjectDTO;
import com.wewe.proflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectApiController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ProjectDTO create(@RequestBody ProjectDTO dto) {
        return projectService.createProject(dto);
    }

    @GetMapping("/owner/{ownerId}")
    public List<ProjectDTO> getByOwner(@PathVariable Long ownerId) {
        return projectService.getProjectsByOwner(ownerId);
    }

    @GetMapping("/{id}")
    public ProjectDTO getById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
