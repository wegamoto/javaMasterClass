package com.wewe.temjaisoft.service;

import com.wewe.temjaisoft.model.Employee;
import com.wewe.temjaisoft.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;

    public void save(Employee e) { repo.save(e); }
    public List<Employee> getAll() { return repo.findAll(); }
    public Optional<Employee> get(Long id) { return repo.findById(id); }
    public void delete(Long id) { repo.deleteById(id); }
}

