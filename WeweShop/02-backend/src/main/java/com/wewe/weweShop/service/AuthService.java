package com.wewe.weweShop.service;

import com.wewe.weweShop.dto.AuthRequest;
import com.wewe.weweShop.dto.RegisterRequest;
import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
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

        // ✅ สร้าง User ใหม่ พร้อมเข้ารหัสรหัสผ่าน โดยใช้ Builder ที่เขียนเอง
        User user = new User.Builder()
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setRoles(List.of("ROLE_ADMIN"))
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
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String jwtToken = jwtService.generateToken(user);

        return Map.of("token", jwtToken, "message", "Login successful");
    }
}

