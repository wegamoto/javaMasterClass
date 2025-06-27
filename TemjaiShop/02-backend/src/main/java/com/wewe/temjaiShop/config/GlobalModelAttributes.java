package com.wewe.temjaiShop.config;

import com.wewe.temjaiShop.model.CartItem;
import com.wewe.temjaiShop.repository.CartItemRepository;
import com.wewe.temjaiShop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {


    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    public GlobalModelAttributes(CartService cartService, CartItemRepository cartItemRepository) {
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName(); // ใช้ email เป็น username
            int cartItemCount = getCartItemCount(username);
            model.addAttribute("cartItemCount", cartItemCount);
        } else {
            model.addAttribute("cartItemCount", 0);
        }
    }

    public Integer getCartItemCount(String username) {
        if (username == null || username.isBlank()) {
            return 0; // หรือจะ throw exception ก็ได้ ถ้าต้องการ
//            throw new IllegalArgumentException("Email must not be null or blank");
        }

        List<CartItem> cartItems = cartItemRepository.findByUser_Username(username);
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }


}

