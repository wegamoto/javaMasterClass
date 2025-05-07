package com.wewe.weweShop.service;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.CartItemRepository;
import com.wewe.weweShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository; // ใช้ UserRepository เพื่อดึง User ตาม Username

    // Method สำหรับคำนวณจำนวนสินค้าทั้งหมดในตะกร้า
    public Integer getTotalQuantityInCart() {
        // ดึงข้อมูล User จาก SecurityContext (UserDetails)
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // หา User จาก Username
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // เรียก CartItemRepository เพื่อคำนวณจำนวนสินค้าจาก User
        return cartItemRepository.sumQuantityByUserId(user.getId());
    }

    public Integer getCartItemCountByEmail(String email) {
        Integer sum = cartItemRepository.sumQuantityByUserEmail(email);
        return sum != null ? sum : 0;
    }

}
