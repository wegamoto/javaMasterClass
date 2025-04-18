package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // เมธอดสำหรับนับจำนวนคำสั่งซื้อที่มีสถานะตามที่กำหนด
    long countByStatus(String status); // นับคำสั่งซื้อที่มีสถานะตามที่ระบุ
}
