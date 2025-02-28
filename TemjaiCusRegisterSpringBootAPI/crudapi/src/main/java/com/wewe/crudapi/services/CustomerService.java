package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // บันทึกข้อมูลลูกค้า
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // ดึงข้อมูลลูกค้าทั้งหมด
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ค้นหาลูกค้าตาม ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // ลบลูกค้าตาม ID
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @GetMapping("/customers")
    public List<Customer> getAllUser() {
        return customerRepository.findAll();
    }

}

