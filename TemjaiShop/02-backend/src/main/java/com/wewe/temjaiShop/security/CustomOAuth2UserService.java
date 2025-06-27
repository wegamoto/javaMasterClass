package com.wewe.temjaiShop.security;

import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.model.enums.AuthProvider;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.repository.RoleRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // เช่น google, facebook
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // ดึง email ถ้าไม่มีจะสร้างจาก ID + provider
        String email = resolveEmail(registrationId, attributes);
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByUsernameOrEmail(email);
        User user;


        if (userOptional.isPresent()) {
            user = userOptional.get();

            // ตรวจสอบว่า provider ตรงกันหรือไม่
            if (!user.getProvider().name().equalsIgnoreCase(registrationId)) {
                throw new OAuth2AuthenticationException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            }

            user = updateExistingUser(user, attributes);
        } else {
            user = registerNewUser(email, registrationId, attributes);
        }

        return UserPrincipal.create(user, attributes);
    }

    private User registerNewUser(String email, String provider, Map<String, Object> attributes) {
        User user = new User();
        user.setEmail(email);
        user.setName((String) attributes.getOrDefault("name", generateUsernameFromEmail(email)));
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
        user.setProviderId((String) attributes.get("id"));
        user.setEnabled(true);
        // TODO: set default role, status, etc. if needed
        // ดึง ROLE_USER จาก DB
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER"));
        user.getRoles().add(role);  // <-- เพิ่ม Role Entity ไม่ใช่ enum

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, Map<String, Object> attributes) {
        existingUser.setName((String) attributes.getOrDefault("name", existingUser.getName()));
        // อัปเดตข้อมูลอื่นๆ เช่น profile picture, locale ฯลฯ
        return userRepository.save(existingUser);
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0] + "_" + UUID.randomUUID().toString().substring(0, 5);
    }

    private String resolveEmail(String provider, Map<String, Object> attributes) {
        if (attributes.containsKey("email")) return (String) attributes.get("email");

        if ("github".equalsIgnoreCase(provider) && attributes.containsKey("login")) {
            return attributes.get("login") + "@github.com";
        }

        if ("facebook".equalsIgnoreCase(provider) && attributes.containsKey("id")) {
            return attributes.get("id") + "@facebook.oauth";  // แก้ typo
        }

        return Optional.ofNullable((String) attributes.get("id"))
                .map(id -> id + "@" + provider + ".oauth")
                .orElse(null);
    }

}

