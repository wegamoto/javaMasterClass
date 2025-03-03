package com.wewe.adsystem.repository;

import com.wewe.adsystem.model.Ad;
import com.wewe.adsystem.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByStatus(Status status);
}

