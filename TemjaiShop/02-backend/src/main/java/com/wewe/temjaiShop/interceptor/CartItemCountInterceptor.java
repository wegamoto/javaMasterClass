package com.wewe.temjaiShop.interceptor;

import com.wewe.temjaiShop.controller.CustomErrorController;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.security.oauth.CustomOAuth2User;
import com.wewe.temjaiShop.service.CartItemCountService;
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

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            Object principal = auth.getPrincipal();
            String email = null;

            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                email = userDetails.getUsername(); // login แบบฟอร์ม
            } else if (principal instanceof CustomOAuth2User oauthUser) {
                email = oauthUser.getUser().getEmail(); // login ผ่าน OAuth2
            }

            if (email != null) {
                int count = cartItemCountService.getTotalItemCountByUsername(email);
                request.getSession().setAttribute("cartItemCount", count);
            } else {
                request.getSession().setAttribute("cartItemCount", 0);
            }

        } else {
            request.getSession().setAttribute("cartItemCount", 0);
        }

        return true;
    }
}

