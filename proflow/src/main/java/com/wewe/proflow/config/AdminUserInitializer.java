package com.wewe.proflow.config;

import com.wewe.proflow.model.Role;
import com.wewe.proflow.model.User;
import com.wewe.proflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@proflow.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@proflow.com");
            admin.setPassword(passwordEncoder.encode("@!admin123456")); // ใช้ BCrypt
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("✅ Default admin created: admin@proflow.com / admin123");
        }
        // ✅ สร้าง Contractor ถ้ายังไม่มี
        if (userRepository.findByEmail("contractor1@proflow.com").isEmpty()) {
            User contractor = new User();
            contractor.setName("John Contractor");
            contractor.setEmail("contractor1@proflow.com");
            contractor.setPassword(passwordEncoder.encode("contractor123"));
            contractor.setRole(Role.CONTRACTOR);

            userRepository.save(contractor);
            System.out.println("✅ Default contractor created: contractor1@proflow.com / contractor123");
        }
    }
}

