package com.wewe.weweShop.service;

import com.wewe.weweShop.dto.AuthRequest;
import com.wewe.weweShop.dto.RegisterRequest;
import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // ✅ REGISTER
    public Map<String, String> register(RegisterRequest request) {

        // ✅ ตรวจสอบว่า email ซ้ำหรือไม่
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        // ✅ สร้าง User ใหม่ พร้อมเข้ารหัสรหัสผ่าน
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        // ✅ บันทึกผู้ใช้ใหม่ลงฐานข้อมูล
        userRepository.save(user);

        // ✅ สร้าง JWT token ให้ผู้ใช้
        String jwtToken = jwtService.generateToken(user);

        // ✅ ส่ง token กลับไปให้ client
        return Map.of(
                "token", jwtToken,
                "message", "Registration successful"
        );
    }

    // ✅ LOGIN
    public Map<String, String> login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String jwtToken = jwtService.generateToken(user);

        return Map.of("token", jwtToken, "message", "Login successful");
    }
}

