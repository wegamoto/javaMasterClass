package com.wewe.temjaisoft.repository.inventory;

import com.wewe.temjaisoft.model.inventory.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
}

