package com.wewe.weweShop.config;

import com.wewe.weweShop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartItemRepository cartItemRepository;

    @ModelAttribute("cartItemCount")
    public int cartItemCount(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return cartItemRepository.countByUserUsername(userDetails.getUsername());
        }
        return 0;
    }
}

