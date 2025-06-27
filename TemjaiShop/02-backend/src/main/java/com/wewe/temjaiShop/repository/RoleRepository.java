package com.wewe.temjaiShop.repository;

import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}
