package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockLogRepository extends JpaRepository<StockLog, Long> {
}
