package com.wewe.temjaiShop.security.oauth;

import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.model.enums.AuthProvider;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.repository.RoleRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    public OAuth2SuccessHandler(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String provider = token.getAuthorizedClientRegistrationId();
        String email = resolveEmail(provider, oAuth2User);
        String name = oAuth2User.getAttribute("name");

        try {

            // ตรวจสอบว่ามีผู้ใช้อยู่แล้วหรือไม่
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setEmail(email);
                user.setFullName(name != null ? name : "Unknown");

                // ป้องกัน null ในกรณี email เป็น null
                String username = generateUsernameFromEmail(email);
                user.setUsername(Objects.requireNonNullElseGet(username, () -> "user" + System.currentTimeMillis()));

                user.setProvider(convertToProvider(provider));
                user.setPassword(UUID.randomUUID().toString()); // ✅ เพิ่ม fake password ป้องกัน null
                Role role = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER"));
                user.setRoles(Set.of(role));

                System.out.println("Saving user: " + user.getEmail() + " with provider: " + user.getProvider());

                userRepository.save(user);
            }

            // Redirect ไปหน้า homepage หรืออื่น ๆ
            String referer = request.getHeader("referer");
            if (referer != null && referer.contains("localhost")) {
                response.sendRedirect("http://localhost:8080/dashboard");
            } else {
                response.sendRedirect(frontendBaseUrl + "/dashboard");
            }

        } catch (Exception e) {
            // หากมีข้อผิดพลาดในการลงทะเบียน ให้ส่งไปหน้า login พร้อม error message
            response.sendRedirect("/login?error=registration_failed");
        }
    }

    private String resolveEmail(String provider, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isEmpty()) {
            return email;
        }

        // fallback สำหรับ GitHub
        if ("github".equalsIgnoreCase(provider)) {
            String login = oAuth2User.getAttribute("login");
            return (login != null ? login : "unknown_" + System.currentTimeMillis()) + "@github.com";
        }

        // fallback สำหรับ Facebook
        if ("facebook".equalsIgnoreCase(provider)) {
            String id = oAuth2User.getAttribute("id");
            return (id != null ? id : "unknown_" + System.currentTimeMillis()) + "@facebook.oath";
        }

        // fallback ทั่วไป
        String id = oAuth2User.getAttribute("id");
        return (id != null ? id : "unknown_" + System.currentTimeMillis()) + "@" + provider + ".oath";
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

