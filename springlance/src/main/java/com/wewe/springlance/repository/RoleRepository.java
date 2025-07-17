package com.wewe.springlance.repository;

import com.wewe.springlance.model.Role;
import com.wewe.springlance.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
