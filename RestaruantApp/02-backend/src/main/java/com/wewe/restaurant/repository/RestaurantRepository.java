package com.wewe.restaurant.repository;

import com.wewe.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // หา Restaurant ตามชื่อร้าน
    Optional<Restaurant> findByName(String name);

    // หา Restaurant ตามเบอร์โทร
    Optional<Restaurant> findByPhoneNumber(String phoneNumber);
}

