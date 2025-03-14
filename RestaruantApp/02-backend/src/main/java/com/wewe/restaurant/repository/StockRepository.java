package com.wewe.restaurant.repository;

import com.wewe.restaurant.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    // สามารถเพิ่ม query method ได้ เช่น การค้นหาสต๊อกโดยชื่อ
    Stock findByName(String name);
}
