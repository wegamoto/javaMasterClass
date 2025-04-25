package com.wewe.weweShop.repository;

import com.wewe.weweShop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserEmail(String userEmail);
    CartItem findByUserEmailAndProductId(String userEmail, Long productId);
    void deleteByUserEmailAndProductId(String userEmail, Long productId);
}
