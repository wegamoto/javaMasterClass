package com.wewe.proflow.service;

import com.wewe.proflow.model.ProjectPhase;

import java.util.List;

public interface ProjectPhaseService {
    List<ProjectPhase> findAll();
    List<ProjectPhase> findByProjectId(Long projectId);
}

