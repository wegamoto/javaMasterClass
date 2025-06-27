package com.servix.maintenance.repository;

import com.servix.maintenance.model.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SparePartRepository extends JpaRepository<SparePart, Long> {
    List<SparePart> findByNameContainingIgnoreCaseOrPartNumberContainingIgnoreCase(String name, String partNumber);
}
