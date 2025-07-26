package com.wewe.swiftaid.repository;

import com.wewe.swiftaid.entity.MedicalSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalSupplyRepository extends JpaRepository<MedicalSupply, Long> {
    // ✅ คุณสามารถเพิ่มเมทอดค้นหาเฉพาะทางเพิ่มเติมได้ เช่น:
    // List<MedicalSupply> findByNameContainingIgnoreCase(String keyword);
    // List<MedicalSupply> findByAssignedTeamId(Long teamId);
}

