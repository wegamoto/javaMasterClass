package com.wewe.proflow.service;

import com.wewe.proflow.dto.ProjectDTO;
import com.wewe.proflow.model.Project;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO dto);
    List<ProjectDTO> getProjectsByOwner(Long ownerId);
    ProjectDTO getProjectById(Long id);
    void deleteProject(Long id);

    List<ProjectDTO> getProjectsByOwnerPaged(Long ownerId, int page, int size);

    long countProjectsByOwner(Long ownerId);

    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);

    List<Project> findAll();
}
