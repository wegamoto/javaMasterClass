package com.wewe.marketflow.config;

import com.wewe.marketflow.model.User;
import com.wewe.marketflow.model.UserRole;
import com.wewe.marketflow.repository.UserRepository;
import com.wewe.marketflow.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UserRoleRepository roleRepo;
    private final UserRepository userRepo;

    public InitialDataLoader(UserRoleRepository roleRepo, UserRepository userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) {
        loadRoles();
        loadAdminUser();
    }

    private void loadRoles() {
        createRole("ADMIN", "ผู้ดูแลระบบ");
        createRole("DIRECTOR", "ผู้อำนวยการการตลาด");
        createRole("STAFF", "เจ้าหน้าที่");
    }

    private void loadAdminUser() {
        if (userRepo.findByEmail("admin@market.com").isEmpty()) {
            UserRole adminRole = roleRepo.findByName("ADMIN").orElseThrow();
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@market.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            admin.setRole(adminRole);
            userRepo.save(admin);
        }
    }

    private void createRole(String name, String desc) {
        if (roleRepo.findByName(name).isEmpty()) {
            UserRole role = new UserRole();
            role.setName(name);
            role.setDescription(desc);
            roleRepo.save(role);
        }
    }
}
