package com.wewe.temjaisoft.repository;

import com.wewe.temjaisoft.model.Department;
import com.wewe.temjaisoft.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // ✅ ตัวอย่างเมธอดเพิ่มเติม (Optional)
    Employee findByEmail(String email);

    List<Employee> findByDepartment(Department department);

    List<Employee> findByDepartment_Id(Long id);
}

