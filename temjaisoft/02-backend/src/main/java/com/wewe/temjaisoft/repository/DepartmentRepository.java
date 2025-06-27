package com.wewe.temjaisoft.repository;

import com.wewe.temjaisoft.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

