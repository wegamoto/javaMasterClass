package com.wewe.weweShop.interceptor;

import com.wewe.weweShop.service.CartItemCountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CartItemCountInterceptor implements HandlerInterceptor {

    private final CartItemCountService cartItemCountService;

    public CartItemCountInterceptor(CartItemCountService cartItemCountService) {
        this.cartItemCountService = cartItemCountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String email = auth.getName(); // ถ้าใช้ email เป็น username
            int count = cartItemCountService.getTotalItemCountByUsername(email);
            request.getSession().setAttribute("cartItemCount", count);
        } else {
            request.getSession().setAttribute("cartItemCount", 0);
        }
        return true;
    }
}

