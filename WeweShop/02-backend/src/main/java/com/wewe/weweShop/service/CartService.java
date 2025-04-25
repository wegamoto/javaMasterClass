package com.wewe.weweShop.service;

import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.repository.CartItemRepository;
import com.wewe.weweShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<CartItem> getCartItems(String userEmail) {
        String email = getCurrentUserEmail();
        return cartItemRepository.findByUserEmail(email);
    }

    public void addToCart(String userEmail, Long productId, int quantity) {
        // ค้นหาสินค้าในฐานข้อมูล
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ค้นหาสินค้าที่มีในตะกร้าของผู้ใช้
        CartItem existingItem = cartItemRepository.findByUserEmailAndProductId(userEmail, productId);

        if (existingItem != null) {
            // เพิ่มจำนวนสินค้าที่มีในตะกร้า
            existingItem.setQuantity(existingItem.getQuantity() + quantity);

            // คำนวณ total ใหม่
            BigDecimal total = existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity()));
            existingItem.setTotal(total);  // อัปเดต total ใหม่

            // บันทึกข้อมูลสินค้าที่อัปเดตในตะกร้า
            cartItemRepository.save(existingItem);
        } else {
            // ถ้ายังไม่มีสินค้าในตะกร้าให้สร้างสินค้าใหม่
            CartItem newItem = new CartItem();
            newItem.setUserEmail(userEmail);
            newItem.setProductId(productId);
            newItem.setProductName(product.getName());
            newItem.setPrice(BigDecimal.valueOf(product.getPrice()));
            newItem.setQuantity(quantity);

            // คำนวณ total สำหรับสินค้ารายการใหม่
            BigDecimal total = newItem.getPrice().multiply(BigDecimal.valueOf(newItem.getQuantity()));
            newItem.setTotal(total);  // อัปเดต total ใหม่

            // บันทึกสินค้าใหม่ในตะกร้า
            cartItemRepository.save(newItem);
        }
    }

    public void updateCartItem(Long productId, int quantity) {
        String email = getCurrentUserEmail();
        CartItem item = cartItemRepository.findByUserEmailAndProductId(email, productId);
        if (item != null) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void removeCartItem(Long productId) {
        String email = getCurrentUserEmail();
        cartItemRepository.deleteByUserEmailAndProductId(email, productId);
    }

    public void clearCart(String userEmail) {
        String email = getCurrentUserEmail();
        List<CartItem> items = cartItemRepository.findByUserEmail(email);
        cartItemRepository.deleteAll(items);
    }
}
