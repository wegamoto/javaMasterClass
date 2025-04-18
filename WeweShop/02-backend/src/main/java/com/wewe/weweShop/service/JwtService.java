package com.wewe.weweShop.service;

import com.wewe.weweShop.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
//    private static final String SECRET_KEY = "your-very-secret-key-1234567890";  // ใช้อักขระพอสมควร
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 วัน

    // สร้าง JWT Token
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    // ดึง username จาก Token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ตรวจสอบว่า token valid หรือไม่
    public boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    // ตรวจสอบว่า token หมดอายุหรือไม่
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ดึง expiration จาก token
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}


