package com.wewe.marketflow.config;

import com.wewe.marketflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminPasswordUpdater implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public AdminPasswordUpdater(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
//        userRepo.findByEmail("admin@market.com").ifPresent(user -> {
//            String newPassword = "@!admin123456"; // ตั้งรหัสใหม่
//            user.setPassword(encoder.encode(newPassword));
//            userRepo.save(user);
//            System.out.println("✅ Updated admin password successfully.");
//        });
    }
}

