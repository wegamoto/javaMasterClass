package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.Cart;
import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // หา CartItem ทั้งหมดของ user คนหนึ่ง
    List<CartItem> findByUserEmail(String userEmail);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // หา CartItem ชิ้นเดียว จาก user + productId (กัน user ใส่สินค้าซ้ำ)
    CartItem findByUserEmailAndProductId(String userEmail, Long productId);

    int countByUserUsername(String username); // ใช้ได้เลย

    Integer countByUser_Id(Long userId); // ตัวอย่างอื่นที่ใช้ Entity reference

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.product.id = :productId")
    void updateCartItemQuantity(Long productId, int quantity);

    @Query("SELECT SUM(c.quantity) FROM CartItem c WHERE c.user.id = :userId")
    Integer sumQuantityByUserId(Long userId);  // ปรับ query ให้ใช้ `c.user.id`

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.userEmail = :userEmail")
    Integer sumQuantityByUserEmail(@Param("userEmail") String userEmail); // ✔ OK

    // ลบ CartItem ทั้งหมดของ user (เช่น ตอน checkout เสร็จ)
    void deleteByUserEmailAndProductId(String userEmail, Long productId);

    void deleteByProductId(Long productId);

    void deleteByUserEmail(String userEmail);

}
