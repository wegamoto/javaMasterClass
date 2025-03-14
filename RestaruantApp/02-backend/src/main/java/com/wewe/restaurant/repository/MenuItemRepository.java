package com.wewe.restaurant.repository;

import com.wewe.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
//    Optional<MenuItem> findById(Long id);
}
