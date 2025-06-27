package com.wewe.temjaiShop.repository;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.model.enums.RoleName;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);



    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findAllByRole(@Param("roleName") RoleName roleName);


    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    // ค้นหา User ที่มี role เป็น ADMIN
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = com.wewe.temjaiShop.model.enums.RoleName.ROLE_ADMIN")
    List<User> findAllAdmins();


    @Query(value = """
    SELECT * FROM users u 
    JOIN user_roles ur ON u.id = ur.user_id 
    JOIN roles r ON ur.role_id = r.id
    WHERE r.name = 'ROLE_ADMIN'
    """, nativeQuery = true)
    List<User> findAllAdminsNative();

    // ตัวอย่าง: อัพเดตค่า roles ให้เป็น ROLE_USER ถ้า roles เป็น null
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.roles = :defaultRoles WHERE u.roles IS NULL")
    int updateNullRoles(Set<RoleName> defaultRoles);

    // หรือถ้าต้องการอัพเดตคอลัมน์อื่น เช่น phoneNumber ที่เป็น null
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.phoneNumber = :defaultPhone WHERE u.phoneNumber IS NULL")
    int updateNullPhoneNumber(String defaultPhone);

}
