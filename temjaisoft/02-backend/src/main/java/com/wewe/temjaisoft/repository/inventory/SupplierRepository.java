package com.wewe.temjaisoft.repository.inventory;

import com.wewe.temjaisoft.model.inventory.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);
}

