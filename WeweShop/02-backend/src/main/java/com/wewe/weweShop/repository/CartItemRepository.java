package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // หา CartItem ทั้งหมดของ user คนหนึ่ง
    List<CartItem> findByUserEmail(String userEmail);

    // หา CartItem ชิ้นเดียว จาก user + productId (กัน user ใส่สินค้าซ้ำ)
    CartItem findByUserEmailAndProductId(String userEmail, Long productId);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.product.id = :productId")
    void updateCartItemQuantity(Long productId, int quantity);

    // ลบ CartItem ทั้งหมดของ user (เช่น ตอน checkout เสร็จ)
    void deleteByUserEmailAndProductId(String userEmail, Long productId);

    void deleteByProductId(Long productId);

    void deleteByUserEmail(String userEmail);


}
