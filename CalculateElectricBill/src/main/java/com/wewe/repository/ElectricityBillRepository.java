package com.wewe.repository;

import com.wewe.model.ElectricityBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ElectricityBillRepository extends JpaRepository<ElectricityBill, Long> {
    // สามารถเพิ่มฟังก์ชันการค้นหาตามความต้องการ
    // ค้นหาค่าไฟฟ้าตามช่วงวันที่
    List<ElectricityBill> findByBillDateBetween(LocalDate startDate, LocalDate endDate);
}
