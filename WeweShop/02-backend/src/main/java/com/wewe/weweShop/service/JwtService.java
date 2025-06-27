package com.wewe.weweShop.service;

import com.wewe.weweShop.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 วัน

    // ✅ ใช้ Keys.hmacShaKeyFor เพื่อให้แน่ใจว่า key ยาวพอ
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
    }

    // ✅ เข้ารหัส Secret Key ด้วย Base64
    private String getEncodedSecret() {
        return Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    // ✅ สร้าง Token พร้อม roles
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())); // ✅ FIXED!

        System.out.println("🎯 Token is generated with authorities: " + claims.get("authorities"));


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    // ✅ ดึง Username จาก Token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ ตรวจสอบความถูกต้องของ Token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // ✅ ใช้ใน JwtFilter เพื่ออ่านข้อมูล roles
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @PostConstruct
    public void debugSecretKey() {
        System.out.println("🔍 Loaded SECRET_KEY: " + SECRET_KEY);
    }
}
