package com.wewe.temjaiShop.repository;

import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // เมธอดสำหรับนับจำนวนคำสั่งซื้อที่มีสถานะตามที่กำหนด
    long countByStatus(String status); // นับคำสั่งซื้อที่มีสถานะตามที่ระบุ

    Optional<Order> findByIdAndUser(Long id, User user);

    List<Order> findByUser(User user);

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByUserId(Long userId);

    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);

    // หา order ตาม userEmail (เพิ่ม)
    List<Order> findByUserEmail(String userEmail);

    List<Order> findByUserUsername(String username);


}
