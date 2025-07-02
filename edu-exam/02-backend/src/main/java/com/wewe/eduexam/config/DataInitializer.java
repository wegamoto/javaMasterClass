// Path: src/main/java/com/wewe/eduexam/DataInitializer.java
package com.wewe.eduexam.config;

import com.wewe.eduexam.model.Role;
import com.wewe.eduexam.model.User;
import com.wewe.eduexam.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("dev")
@Configuration
public class DataInitializer implements ApplicationRunner {

    @Bean
    public ApplicationRunner initializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Optional<User> existingAdmin = userRepository.findByUsername("admin");

            if (existingAdmin.isEmpty()) {
                // กรณีไม่มี admin -> สร้างใหม่
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("Good*123456"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("✅ Created default admin user");
            } else {
                // กรณีมีอยู่แล้ว -> อัปเดตรหัสผ่านใหม่
                User admin = existingAdmin.get();
                admin.setPassword(passwordEncoder.encode("Good*123456")); // ตั้งรหัสใหม่
                userRepository.save(admin);
                System.out.println("🔁 Updated admin password");
            }
        };
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // รันเฉพาะเมื่อ profile = dev
        // เช่น reset รหัสผ่าน admin
    }
}

