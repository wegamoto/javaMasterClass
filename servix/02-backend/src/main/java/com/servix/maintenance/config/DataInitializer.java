package com.servix.maintenance.config;

import com.servix.maintenance.model.Role;
import com.servix.maintenance.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        String roleName = "ROLE_TECHNICIAN";
        boolean exists = roleRepository.findByName(roleName).isPresent();
        if (!exists) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("Role 'ROLE_TECHNICIAN' created.");
        }
    }
}

