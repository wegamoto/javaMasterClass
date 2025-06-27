package com.servix.maintenance.bootstrap;

import com.servix.maintenance.model.Role;
import com.servix.maintenance.model.User;
import com.servix.maintenance.repository.RoleRepository;
import com.servix.maintenance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // ✅ สร้าง ROLE_ADMIN ถ้ายังไม่มี
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });

            // ✅ สร้าง ROLE_TECHNICIAN ถ้ายังไม่มี
            Role techRole = roleRepository.findByName("ROLE_TECHNICIAN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_TECHNICIAN");
                        return roleRepository.save(role);
                    });

            // ✅ สร้าง User admin
            User adminUser = User.builder()
                    .username("admin")
                    .password(bCryptPasswordEncoder.encode("@!admin123"))
                    .fullName("Admin User")
                    .roles(Set.of(adminRole))
                    .enabled(true)
                    .build();
            userRepository.save(adminUser);

            // ✅ สร้าง User technician
            User technicianUser = User.builder()
                    .username("tech1")
                    .password(bCryptPasswordEncoder.encode("123456"))
                    .fullName("Technician One")
                    .roles(Set.of(techRole))
                    .enabled(true)
                    .build();
            userRepository.save(technicianUser);
        }
    }
}
