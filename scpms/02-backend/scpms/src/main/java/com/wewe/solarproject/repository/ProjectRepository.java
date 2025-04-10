package com.wewe.solarproject.repository;

import com.wewe.solarproject.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // สามารถเพิ่ม custom query ได้ที่นี่ เช่น findByStatus
}

