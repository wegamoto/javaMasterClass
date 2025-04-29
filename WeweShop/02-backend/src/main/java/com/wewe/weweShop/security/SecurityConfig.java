package com.wewe.weweShop.security;

import com.wewe.weweShop.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // เปิดใช้งาน @PreAuthorize และ @Secured

public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // กำหนด Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // กำหนด Authentication Provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // สำหรับใช้ AuthenticationManager ใน Controller
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // กำหนด Security Filter Chain
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/products/**").hasRole("ADMIN") // ADMIN เท่านั้นที่เข้าถึงได้
//                        .anyRequest().authenticated()
//                )
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // เปิด session แบบจำเป็น
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()  // หน้า login, css, js, และ images สามารถเข้าถึงได้ทุกคน
                        .requestMatchers("/api/auth/**").permitAll() // // API สำหรับการยืนยันตัวตนสามารถเข้าถึงได้ทุกคน
                        .requestMatchers("/dashboard").authenticated() // หน้า dashboard ต้องล็อกอินก่อนถึงจะเข้าถึงได้
                        .requestMatchers("/orders/list").authenticated() // หน้า order/list ต้องล็อกอินก่อนถึงจะเข้าถึงได้
                        .requestMatchers("/profile").authenticated() // เพิ่มการยืนยันตัวตนสำหรับหน้า /profile
                        .requestMatchers("/recommendations").authenticated() // ดึงรายการสินค้าแนะนำมาแสดง
                        .anyRequest().authenticated() // // ทุกคำขออื่น ๆ ต้องล็อกอิน
                )
                .formLogin(form -> form
                        .loginPage("/login") // // หน้า login ของคุณ
                        .defaultSuccessUrl("/dashboard", true) // หลังจากล็อกอินสำเร็จจะไปที่หน้า dashboard
                        .permitAll()
                )
                .logout(logout -> logout // เมื่อออกจากระบบจะกลับไปที่หน้า login
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // ✅ เพิ่ม JWT Filter

        return http.build();
    }
}
