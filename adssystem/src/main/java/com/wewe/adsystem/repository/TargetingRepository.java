package com.wewe.adsystem.repository;

import com.wewe.adsystem.model.Targeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetingRepository extends JpaRepository<Targeting, Long> {
    List<Targeting> findByLocation(String location);
    List<Targeting> findByGender(String gender);
}

