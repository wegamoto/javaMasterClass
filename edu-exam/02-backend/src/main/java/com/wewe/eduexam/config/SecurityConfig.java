package com.wewe.eduexam.config;

import com.wewe.eduexam.security.JwtFilter;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // ✅ Constructor injection สำหรับ JwtFilter
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // ✅ ใช้ session ปกติสำหรับเว็บ
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/login.html", "/register","/register.html", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // API สำหรับ register/login
                        .requestMatchers(HttpMethod.POST,"/choices/save").permitAll()


                        .requestMatchers("/student/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN") // เสริมความปลอดภัยสำหรับ path admin
                        .requestMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN") // ตัวอย่างสำหรับครู
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        // กำหนดหน้า 403 (Access Denied) เมื่อ user ไม่มีสิทธิ์
                        .accessDeniedPage("/access-denied")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/student/exams", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        // ✅ เพิ่ม JWT Filter สำหรับ API ก่อน UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
