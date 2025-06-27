package com.servix.maintenance.service;

import com.servix.maintenance.model.User;
import com.servix.maintenance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findUsersByRole(String roleName) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase(roleName)))
                .toList();
    }

    public List<User> getTechnicians() {
        return userRepository.findByRoles_Name("ROLE_TECHNICIAN");
    }
}

