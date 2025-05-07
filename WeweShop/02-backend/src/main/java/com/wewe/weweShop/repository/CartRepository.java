package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Cart;
import com.wewe.weweShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);  // Use the User entity in the query

    // ✅ ถ้าอยากหา cart ด้วย username ต้องใช้ @Query แบบนี้
    @Query("SELECT c FROM Cart c WHERE c.user.username = :username")
    Optional<Cart> findByUsername(@Param("username") String username);

    // แก้ไขให้ใช้ c.user.id แทน ci.userId
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user.id = :userId")
    Integer sumQuantityByUserId(@Param("userId") Long userId);

}
