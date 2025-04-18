package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByStockLessThan(int stock);
    List<Product> findByStockLessThan(int stock);
}
