package com.wewe.crudapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)  // ปิด CORS (หรือใช้ CorsConfiguration)
                .csrf(AbstractHttpConfigurer::disable)  // ปิด CSRF เพื่อให้ API ใช้ POST ได้
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ไม่ใช้ Session
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()  // อนุญาตให้เข้าถึง API ลูกค้า
//                        .anyRequest().authenticated()  // อื่นๆ ต้องล็อกอิน
                                .anyRequest().permitAll() // dev อนุญาตทุกหน้า
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("1234")  // เปลี่ยนรหัสผ่านตามต้องการ
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

