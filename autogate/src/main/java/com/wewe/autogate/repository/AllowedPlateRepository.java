package com.wewe.autogate.repository;

import com.wewe.autogate.model.AllowedPlate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllowedPlateRepository extends JpaRepository<AllowedPlate, Long> {
    boolean existsByPlateNumber(String plateNumber);
    Optional<AllowedPlate> findByPlateNumber(String plateNumber);
}

