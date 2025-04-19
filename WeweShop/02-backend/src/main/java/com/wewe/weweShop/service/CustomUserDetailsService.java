package com.wewe.weweShop.service;

import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // ตรวจสอบ role และเพิ่ม "ROLE_" ถ้าไม่มี
        String role = user.getRole();
        if(!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(), // account status
                true, // account is not expired
                true, // credentials are not expired
                true, // account is not locked
                Collections.singletonList(new SimpleGrantedAuthority(role)) // Add authorities
        );
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())  // ตรวจสอบว่ารหัสผ่านใน DB ถูกเข้ารหัสด้วย BCrypt
//                .roles(user.getRole().replace("ROLE_", ""))
//                .build();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // ค้นหาผู้ใช้จากฐานข้อมูลตามชื่อผู้ใช้ (อีเมล หรือ username)
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//
//        // สร้างและส่งกลับ UserDetails ซึ่งจะถูกใช้ในกระบวนการ authentication
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                true, // account is not locked
//                true, // account is not expired
//                true, // credentials are not expired
//                true, // account is enabled
//                user.getAuthorities() // กำหนด authorities จาก roles ที่กำหนดไว้ใน User
//        );
//    }


}
