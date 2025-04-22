package com.wewe.weweShop.controller;

import com.wewe.weweShop.dto.AuthRequest;
import com.wewe.weweShop.dto.RegisterRequest;
import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.UserRepository;
import com.wewe.weweShop.security.JwtUtils;
import com.wewe.weweShop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")  // ใช้ URL ที่เหมาะสมสำหรับ API
public class AuthController {

    // login + register API

    private final AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login method
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

//        // ทดสอบ database user
//        System.out.println("Login attempt: username = " + username + ", password = " + password);
//        User user = new User();
//        user.setUsername("devtest8");
//        user.setPassword(passwordEncoder.encode("@!testpassword123456"));  // ใช้ BCryptEncoder หรือแบบที่ระบบใช้จริง
//        user.setEmail("devtest8@example.com");
//        user.setRoles(List.of("ADMIN")); // หรือไม่มี "ROLE_" นำหน้า
//        userRepository.save(user);



        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // First Register method (used for registering with User object directly)
    @PostMapping("/register/simple")
    public User registerSimple(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    // Second Register method (used for registering with RegisterRequest DTO)
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(authService.register(request));
//    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(List.of("ROLE_ADMIN"));  // กำหนด roles เป็น "USER" ให้กับผู้ใช้ใหม่

        userRepository.save(newUser);  // บันทึกผู้ใช้ใหม่ลงฐานข้อมูล

        return ResponseEntity.ok("User registered successfully");
    }

}

