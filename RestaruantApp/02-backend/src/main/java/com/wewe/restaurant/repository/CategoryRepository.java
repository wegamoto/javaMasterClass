package com.wewe.restaurant.repository;

import com.wewe.restaurant.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // คุณสามารถเพิ่ม query methods เพิ่มเติมที่ต้องการ
}
