package com.servix.maintenance.repository;

import com.servix.maintenance.model.WorkRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRequestRepository extends JpaRepository<WorkRequest, Long> {
}
