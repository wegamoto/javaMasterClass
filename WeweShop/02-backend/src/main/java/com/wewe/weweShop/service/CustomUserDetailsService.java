package com.wewe.weweShop.service;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 🔍 ดึงผู้ใช้จาก DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 🛡️ แปลง roles เป็น GrantedAuthority พร้อม fallback
        List<SimpleGrantedAuthority> authorities =
                user.getRoles() != null && !user.getRoles().isEmpty()
                        ? user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // 🧾 Debug log
        System.out.println("✅ Loaded user: " + user.getUsername());
        System.out.println("📛 Roles: " + user.getRoles());

        // ✅ สร้าง Spring Security UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
