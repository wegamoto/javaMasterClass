package com.wewe.weweShop.security;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.model.enums.AuthProvider;
import com.wewe.weweShop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    public OAuth2SuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        try {
            // ตรวจสอบว่า email มีค่าเป็น null หรือไม่
            if (email == null || email.isEmpty()) {
                // หากไม่ได้รับ email จาก provider ให้ปฏิเสธการลงทะเบียนและแสดง error
                response.sendRedirect("/login?error=email_not_provided");
                return;
            }

            // ตรวจสอบว่ามีผู้ใช้อยู่แล้วหรือไม่
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setEmail(email);
                user.setFullName(name != null ? name : "Unknown");

                // ป้องกัน null ในกรณี email เป็น null
                String username = generateUsernameFromEmail(email);
                // กำหนด default username
                user.setUsername(Objects.requireNonNullElseGet(username, () -> "user" + System.currentTimeMillis()));  // กำหนด username

                user.setProvider(convertToProvider(token.getAuthorizedClientRegistrationId()));

                // ใช้ ROLE ที่เป็นมาตรฐาน
                user.setRoles(List.of("ROLE_USER"));

                userRepository.save(user);
            }

            // Redirect ไปหน้า homepage หรืออื่น ๆ
            response.sendRedirect(frontendBaseUrl + "/dashboard");

        } catch (Exception e) {
            // หากมีข้อผิดพลาดในการลงทะเบียน ให้ส่งไปหน้า login พร้อม error message
            response.sendRedirect("/login?error=registration_failed");
        }
    }

    private AuthProvider convertToProvider(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProvider.GOOGLE;
            case "facebook" -> AuthProvider.FACEBOOK;
            default -> AuthProvider.LOCAL;
        };
    }

    // ฟังก์ชันสร้าง username จาก email
    private String generateUsernameFromEmail(String email) {
        if (email != null && !email.isEmpty()) {
            return email.split("@")[0];  // สร้าง username จากส่วนแรกของ email
        }
        return null;  // หาก email เป็น null หรือว่าง
    }
}

