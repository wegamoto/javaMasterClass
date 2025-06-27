package com.servix.maintenance.repository;

import com.servix.maintenance.model.SparePartUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SparePartUsageRepository extends JpaRepository<SparePartUsage, Long> {
}
