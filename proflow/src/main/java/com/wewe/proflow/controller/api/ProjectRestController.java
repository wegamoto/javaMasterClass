package com.wewe.proflow.controller.api;

import com.wewe.proflow.model.ProjectPhase;
import com.wewe.proflow.service.ProjectPhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectPhaseService projectPhaseService;

    @GetMapping("/{projectId}/phases")
    public List<ProjectPhase> getPhasesByProject(@PathVariable Long projectId) {
        return projectPhaseService.findByProjectId(projectId);
    }
}
