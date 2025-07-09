package com.wewe.proflow.repository;

import com.wewe.proflow.model.ProjectPhase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectPhaseRepository extends JpaRepository<ProjectPhase, Long> {
    List<ProjectPhase> findByProjectId(Long projectId);
}

