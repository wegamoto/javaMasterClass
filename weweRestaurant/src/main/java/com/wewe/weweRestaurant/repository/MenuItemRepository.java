package com.wewe.weweRestaurant.repository;

import com.wewe.weweRestaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}

