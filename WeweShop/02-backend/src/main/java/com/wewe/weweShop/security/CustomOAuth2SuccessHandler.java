package com.wewe.weweShop.security;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.model.enums.AuthProvider;
import com.wewe.weweShop.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomOAuth2SuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // ตรวจสอบว่ามีผู้ใช้อยู่แล้วหรือไม่
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setUsername(generateUsernameFromEmail(email));
            user.setProvider(convertToProvider(token.getAuthorizedClientRegistrationId()));

            List<String> roles = new ArrayList<>();
            roles.add("USER");
            user.setRoles(roles);

            userRepository.save(user);
        }

        // Redirect ไปหน้า homepage หรืออื่น ๆ
        response.sendRedirect("/dashboard");
    }

    private AuthProvider convertToProvider(String registrationId) {
        switch (registrationId.toLowerCase()) {
            case "google":
                return AuthProvider.GOOGLE;
            case "facebook":
                return AuthProvider.FACEBOOK;
            default:
                return AuthProvider.LOCAL;
        }
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0] + "_" + System.currentTimeMillis();
    }
}

