package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.dto.AuthRequest;
import com.wewe.temjaiShop.dto.AuthResponse;
import com.wewe.temjaiShop.dto.RegisterRequest;
import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.repository.RoleRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    // ✅ REGISTER
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = Optional.ofNullable(request.getEmail())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException("Email is required."));

        String password = Optional.ofNullable(request.getPassword())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException("Password must not be empty."));

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER"));

        User user = new User.Builder()
                .setUsername(request.getUsername())
                .setEmail(email)
                .setPassword(passwordEncoder.encode(password))
                .setPhoneNumber(request.getPhoneNumber())
                .setFullName(request.getFullName())
                .setRoles(Set.of(role))
                .build();

        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("authorities", user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList()));

        String jwtToken = jwtService.generateToken(user, extraClaims);

        return new AuthResponse(jwtToken, "Registration successful");
    }

    // ✅ LOGIN
    public AuthResponse login(AuthRequest request) {

        String email = Optional.ofNullable(request.getEmail())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException("Email is required."));

        String password = Optional.ofNullable(request.getPassword())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException("Password is required."));

        authenticateUser(email, password);

        User user = getUserByEmail(email);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("authorities", user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList()));

        String jwtToken = jwtService.generateToken(user, extraClaims);

        return new AuthResponse(jwtToken, "Login successful");
    }

    private void authenticateUser(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByUsernameOrEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
}
