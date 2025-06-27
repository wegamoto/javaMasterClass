package com.wewe.temjaiShop.advice;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.CartItemRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.security.oauth.CustomOAuth2User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {


    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public GlobalControllerAdvice(CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void globalAttributes(Model model, HttpSession session) {
        // เพิ่ม username หากผู้ใช้ login อยู่
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("username", auth.getName());
        }

        // เพิ่ม cartItemCount หากมี session
        Integer cartItemCount = (Integer) session.getAttribute("cartItemCount");
        model.addAttribute("cartItemCount", cartItemCount != null ? cartItemCount : 0);
    }

    @ModelAttribute("username")
    public String username(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // หรือ userService.findNameByUsername(authentication.getName());
        }
        return "Guest";
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String emailOrUsername = null;

            if (principal instanceof UserDetails userDetails) {
                emailOrUsername = userDetails.getUsername();
            } else if (principal instanceof CustomOAuth2User oauthUser) {
                emailOrUsername = oauthUser.getEmail(); // หรือ getName()
            }

            if (emailOrUsername != null) {
                Optional<User> userOpt = userRepository.findByUsernameOrEmail(emailOrUsername);
                if (userOpt.isPresent()) {
                    Long userId = userOpt.get().getId();  // ปรับเปลี่ยนเนื่องจากผูก cartItem กับ user ใช้ user_id
                    return cartItemRepository.countByUser_Id(userId);
                }
            }
        }
        return 0;
    }
}

