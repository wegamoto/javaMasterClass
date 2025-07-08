package com.wewe.proflow.repository;

import com.wewe.proflow.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long ownerId);

    // (ถ้าใช้ countProjectsByOwner) นับทั้งหมดของ owner
    long countByOwnerId(Long ownerId);

    // สำหรับดึงเป็นหน้า ๆ ตาม ownerId
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);


}

