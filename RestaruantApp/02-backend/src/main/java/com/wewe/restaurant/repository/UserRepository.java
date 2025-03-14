package com.wewe.restaurant.repository;

import com.wewe.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository มี findById(Long id) มาให้อยู่แล้ว
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

