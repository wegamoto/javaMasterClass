package com.wewe.temjaisoft.repository.inventory;

import com.wewe.temjaisoft.model.inventory.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}

