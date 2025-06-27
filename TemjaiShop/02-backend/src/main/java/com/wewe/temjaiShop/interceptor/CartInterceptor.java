package com.wewe.temjaiShop.interceptor;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.security.oauth.CustomOAuth2User;
import com.wewe.temjaiShop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            Object principal = authentication.getPrincipal();
            String userEmail = null;

            if (principal instanceof UserDetails userDetails) {
                userEmail = userDetails.getUsername(); // จาก login ปกติ
            } else if (principal instanceof CustomOAuth2User oAuthUser) {
                userEmail = oAuthUser.getUser().getEmail(); // จาก OAuth2
            }

            if (userEmail != null) {
                Optional<User> userOptional = userRepository.findByUsernameOrEmail(userEmail);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    int cartCount = cartService.countItemsInCart(user.getId());
                    request.getSession().setAttribute("cartCount", cartCount); // session-scoped
                } else {
                    request.getSession().setAttribute("cartCount", 0);
                }
            } else {
                request.getSession().setAttribute("cartCount", 0);
            }

        } else {
            request.getSession().setAttribute("cartCount", 0);
        }

        return true;
    }
}
