package com.wewe.taxcertify.config;

import com.wewe.taxcertify.model.User;
import com.wewe.taxcertify.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String defaultAdmin = "admin";

            if (userRepository.findByUsername(defaultAdmin).isEmpty()) {
                User admin = User.builder()
                        .username(defaultAdmin)
                        .password(passwordEncoder.encode("@!admin123"))  // 🛡 รหัสผ่านเข้ารหัส
                        .role("ADMIN")
                        .build();
                userRepository.save(admin);
                System.out.println("✅ Default admin user created: username=admin, password=admin123");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }
        };
    }
}

