package com.wewe.weweShop.security;

import com.wewe.weweShop.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wewe.weweShop.service.CustomUserDetailsService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;  // ใช้ JwtService สำหรับตรวจสอบ token

    @Autowired
    private CustomUserDetailsService userDetailsService;  // ใช้ในการโหลดข้อมูลผู้ใช้จากฐานข้อมูล

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // ตรวจสอบว่า header Authorization มีค่า และขึ้นต้นด้วย "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // ข้ามไปยังฟิลเตอร์ถัดไป
            return;
        }

        jwt = authHeader.substring(7);  // ตัดคำว่า "Bearer " ออก
        username = jwtService.extractUsername(jwt);  // ดึง username จาก JWT

        // ตรวจสอบว่า user ยังไม่ได้รับการ authenticate
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);  // โหลดข้อมูลผู้ใช้จากฐานข้อมูล

            // ตรวจสอบว่า token ยัง valid อยู่หรือไม่
            if (jwtService.isTokenValid(jwt, (com.wewe.weweShop.model.User) userDetails)) {
                // สร้าง authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ตั้งค่า authentication ใน SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);  // ส่งคำขอไปยังฟิลเตอร์ถัดไป
    }
}
