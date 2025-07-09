package com.wewe.proflow.service.impl;

import com.wewe.proflow.model.ProjectPhase;
import com.wewe.proflow.repository.ProjectPhaseRepository;
import com.wewe.proflow.service.ProjectPhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectPhaseServiceImpl implements ProjectPhaseService {
    private final ProjectPhaseRepository projectPhaseRepository;

    @Override
    public List<ProjectPhase> findAll() {
        return projectPhaseRepository.findAll();
    }

    @Override
    public List<ProjectPhase> findByProjectId(Long projectId) {
        return projectPhaseRepository.findByProjectId(projectId);
    }
}
