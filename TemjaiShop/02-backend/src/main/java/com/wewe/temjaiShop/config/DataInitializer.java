package com.wewe.temjaiShop.config;

import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            roleRepository.findByName(roleName).orElseGet(() -> {
                return roleRepository.save(new Role(roleName));
            });
        });
    }
}

