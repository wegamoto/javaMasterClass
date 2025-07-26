package com.wewe.swiftaid.repository;

import com.wewe.swiftaid.entity.AmbulanceTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceTeamRepository extends JpaRepository<AmbulanceTeam, Long> {
    // ถ้าต้องการเพิ่ม method เฉพาะ สามารถเพิ่มได้ที่นี่
}
