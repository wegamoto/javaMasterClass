package com.wewe.restaurant.repository;

import com.wewe.restaurant.model.Financial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialRepository extends JpaRepository<Financial, Long> {
    // สามารถเพิ่ม query method ได้ เช่น การค้นหาการเงินตามวันที่
    Financial findByDate(String date);
}

