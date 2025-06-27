package com.proman.proman_erp.repository;

import com.proman.proman_erp.entity.Inventory;
import com.proman.proman_erp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
}
