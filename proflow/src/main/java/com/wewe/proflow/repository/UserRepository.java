package com.wewe.proflow.repository;

import com.wewe.proflow.model.Role;
import com.wewe.proflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // เพิ่มเติมถ้ามีระบบ Auth
    Optional<User> findByEmailAndPassword(String email, String password); // ใช้เฉพาะกรณีไม่มี JWT/Security

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(Role role);

    List<User> findByRole(Role role);

}
