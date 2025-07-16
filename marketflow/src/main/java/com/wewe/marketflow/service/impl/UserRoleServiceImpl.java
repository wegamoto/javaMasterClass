package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.model.UserRole;
import com.wewe.marketflow.repository.UserRoleRepository;
import com.wewe.marketflow.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole getById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserRole not found with ID: " + id));
    }

    @Override
    public List<UserRole> findAll() {
        return List.of();
    }

    @Override
    public Optional<UserRole> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserRole> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public UserRole save(UserRole role) {
        return userRoleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }
}

