package com.wewe.springlance.config;

import com.wewe.springlance.model.Role;
import com.wewe.springlance.model.User;
import com.wewe.springlance.model.enums.RoleName;
import com.wewe.springlance.repository.RoleRepository;
import com.wewe.springlance.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        createRolesIfNotFound();
        createAdminUserIfNotFound();
    }

    private void createRolesIfNotFound() {
        for (RoleName roleName : EnumSet.allOf(RoleName.class)) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(null, roleName, null)));
        }
    }

    private void createAdminUserIfNotFound() {
        String adminEmail = "admin@springlance.com";
        Optional<User> adminOpt = userRepository.findByEmail(adminEmail);
        if (adminOpt.isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("@!admin123")); // ควรเปลี่ยนรหัสให้ปลอดภัยขึ้นในโปรดักชัน
            admin.setFullName("Administrator");
            admin.setRoles(Collections.singleton(adminRole));

            userRepository.save(admin);
            System.out.println("✅ Admin user created: " + adminEmail);
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }
}
