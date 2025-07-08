package com.wewe.proflow.service.impl;

import com.wewe.proflow.dto.OwnerDTO;
import com.wewe.proflow.dto.ProjectDTO;
import com.wewe.proflow.model.Owner;
import com.wewe.proflow.model.Project;
import com.wewe.proflow.repository.OwnerRepository;
import com.wewe.proflow.repository.ProjectRepository;
import com.wewe.proflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public ProjectDTO createProject(ProjectDTO dto) {
        Owner owner = ownerRepository.findById(dto.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + dto.getOwner().getId()));

        Project project = mapToEntity(dto, owner);

        Project saved = projectRepository.save(project);
        return mapToDTO(saved);
    }

    private Project mapToEntity(ProjectDTO dto, Owner owner) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setBudget(dto.getBudget());
        project.setActive(dto.isActive());
        project.setOwner(owner);
        return project;
    }

    @Override
    public List<ProjectDTO> getProjectsByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return mapToDTO(p);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectDTO mapToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setBudget(project.getBudget());
        dto.setActive(project.isActive());
        dto.setOwner(mapToOwnerDTO(project.getOwner()));
        return dto;
    }

    private OwnerDTO mapToOwnerDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setEmail(owner.getEmail());
        return dto;
    }

    @Override
    public List<ProjectDTO> getProjectsByOwnerPaged(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Project> projectPage = projectRepository.findByOwnerId(ownerId, pageable);
        return projectPage.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countProjectsByOwner(Long ownerId) {
        return projectRepository.countByOwnerId(ownerId);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setBudget(projectDTO.getBudget());
        project.setActive(projectDTO.isActive());

        if (projectDTO.getOwner() != null && projectDTO.getOwner().getId() != null) {
            Owner owner = ownerRepository.findById(projectDTO.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found with id: " + projectDTO.getOwner().getId()));
            project.setOwner(owner);
        }

        Project updated = projectRepository.save(project);
        return mapToDTO(updated);
    }
}
