package com.wewe.weweShop.interceptor;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;
import com.wewe.weweShop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class CartInterceptor implements HandlerInterceptor {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername(); // ถ้าใช้ email เป็น username
                Optional<User> userOptional = userService.findByEmail(username);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    int cartCount = cartService.countItemsInCart(user.getId());
                    request.setAttribute("cartCount", cartCount);
                } else {
                    request.setAttribute("cartCount", 0); // ไม่พบ user
                }
            }
        }
        return true;
    }
}

