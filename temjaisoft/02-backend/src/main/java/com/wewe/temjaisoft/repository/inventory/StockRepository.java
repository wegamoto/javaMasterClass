package com.wewe.temjaisoft.repository.inventory;

import com.wewe.temjaisoft.model.inventory.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByProductId(Long productId);
}

