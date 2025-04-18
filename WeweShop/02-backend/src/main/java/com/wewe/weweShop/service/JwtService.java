package com.wewe.weweShop.security;

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

    // คีย์ลับในการเซ็น JWT
    private String secretKey = "your_secret_key_here";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // สร้าง JWT Token
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expiration time
                .signWith(SignatureAlgorithm.HS256, secret) // ใช้ HS256 algorithm
                .compact();
    }

    // ดึง username จาก Token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
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
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}


