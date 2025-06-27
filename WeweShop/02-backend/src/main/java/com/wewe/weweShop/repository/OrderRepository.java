package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // เมธอดสำหรับนับจำนวนคำสั่งซื้อที่มีสถานะตามที่กำหนด
    long countByStatus(String status); // นับคำสั่งซื้อที่มีสถานะตามที่ระบุ

    Optional<Order> findByIdAndUser(Long id, User user);

    List<Order> findByUser(User user);

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByCustomerEmail(String userEmail);

    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);


}
