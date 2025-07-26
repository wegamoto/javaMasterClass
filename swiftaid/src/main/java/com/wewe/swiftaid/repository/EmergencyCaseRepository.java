package com.wewe.swiftaid.repository;

import com.wewe.swiftaid.entity.EmergencyCase;
import com.wewe.swiftaid.entity.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmergencyCaseRepository extends JpaRepository<EmergencyCase, Long> {

    // ดึงข้อมูลตามสถานะเคส เช่น REPORTED, ON_THE_WAY
    List<EmergencyCase> findByStatus(EmergencyStatus status);

    // ค้นหาโดยมีคำว่า...ในสถานที่ (location)
    List<EmergencyCase> findByLocationContainingIgnoreCase(String keyword);

    // เรียงลำดับตามเวลาที่แจ้งล่าสุด
    List<EmergencyCase> findAllByOrderByReportedAtDesc();

    // findAll() มาจาก JpaRepository อยู่แล้ว ไม่ต้องประกาศใหม่

    List<EmergencyCase> findByReportedAtBetweenOrderByReportedAtDesc(LocalDateTime start, LocalDateTime end);

}
