package com.wewe.temjaisoft.service;

import com.wewe.temjaisoft.model.Employee;
import com.wewe.temjaisoft.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }
}
