package com.wewe.temjaiShop.security;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.model.enums.AuthProvider;
import com.wewe.temjaiShop.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name"); // ใช้ name เป็น username ได้

        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId().toUpperCase();

        if (email != null) {
            Optional<User> optionalUser = userRepository.findByUsernameOrEmail(email);

            if (optionalUser.isEmpty()) {
                // ➕ สร้าง user ใหม่ถ้ายังไม่มีใน DB
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(name != null ? name : email);
                newUser.setProvider(AuthProvider.valueOf(registrationId)); // หรือ "FACEBOOK" ขึ้นอยู่กับ provider
                newUser.setProviderId(oAuth2User.getName());
                newUser.setEnabled(true);
                userRepository.save(newUser);
            }
        }

        // Redirect ไปหน้าแรก หรือหน้าที่ต้องการ
        response.sendRedirect("/");
    }
}

