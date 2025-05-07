package com.wewe.weweShop.config;

import com.wewe.weweShop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            String userEmail = authentication.getName(); // ใช้ email เป็น username
            int cartItemCount = cartService.getCartItemCount(userEmail);
            model.addAttribute("cartItemCount", cartItemCount);
        } else {
            model.addAttribute("cartItemCount", 0);
        }
    }
}

