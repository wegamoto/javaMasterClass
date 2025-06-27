package com.wewe.temjaiShop.repository;

import com.wewe.temjaiShop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // เพิ่มเมธอดเสริม ถ้าต้องการ เช่น ดึงรายการ OrderItem ตาม Order ID
    List<OrderItem> findByOrderId(Long orderId);
}

