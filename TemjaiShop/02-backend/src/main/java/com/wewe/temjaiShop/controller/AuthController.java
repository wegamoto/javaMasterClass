package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.security.jwt.JwtUtils;
import com.wewe.temjaiShop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
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
        try {
            String username = body.get("username");
            String password = body.get("password");

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("roles", userDetails.getAuthorities());
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
        }
    }

//    // First Register method (used for registering with User object directly)
//    @PostMapping("/register/simple")
//    public User registerSimple(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
//        User newUser = new User();
//        newUser.setUsername(request.getUsername());
//        newUser.setEmail(request.getEmail());
//        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
//        newUser.setRoles(List.of("ROLE_USER"));  // กำหนด roles เป็น "USER" ให้กับผู้ใช้ใหม่
//
//        userRepository.save(newUser);  // บันทึกผู้ใช้ใหม่ลงฐานข้อมูล
//
//        return ResponseEntity.ok("User registered successfully");
//    }
}

