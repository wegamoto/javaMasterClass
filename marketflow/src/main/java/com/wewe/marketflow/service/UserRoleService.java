package com.wewe.marketflow.service;

import com.wewe.marketflow.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {

    List<UserRole> getAllRoles();

    UserRole getById(Long id);

    List<UserRole> findAll();

    Optional<UserRole> findById(Long id);

    Optional<UserRole> findByName(String name);

    UserRole save(UserRole role);

    void deleteById(Long id);

    void delete(Long id);
}

