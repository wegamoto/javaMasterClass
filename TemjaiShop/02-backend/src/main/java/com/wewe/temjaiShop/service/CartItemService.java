package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.CartItemRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {


    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository; // ใช้ UserRepository เพื่อดึง User ตาม Username

    public CartItemService(CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    // Method สำหรับคำนวณจำนวนสินค้าทั้งหมดในตะกร้า
    public Integer getTotalQuantityInCart() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usernameOrEmail;

        if (principal instanceof UserDetails) {
            // Local Login
            usernameOrEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            // OAuth2 Login (Google, Facebook, etc.)
            OAuth2User oauthUser = (OAuth2User) principal;

            // เปลี่ยนตาม provider ที่ใช้งาน เช่น "email", "preferred_username"
            usernameOrEmail = (String) oauthUser.getAttribute("email");

            if (usernameOrEmail == null) {
                throw new RuntimeException("Email not found in OAuth2 principal.");
            }
        } else {
            throw new RuntimeException("Cannot determine email from logged-in user.");
        }

        // หา User จาก Username หรือ Email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // เรียก repository โดยส่ง email ของ user
        String userEmail = user.getEmail();
        Integer sum = cartItemRepository.sumQuantityByUserEmail(userEmail);

        return sum != null ? sum : 0;
    }

    public Integer getCartItemCountByEmail(String email) {
        Integer sum = cartItemRepository.sumQuantityByUserEmail(email);
        return sum != null ? sum : 0;
    }

}
