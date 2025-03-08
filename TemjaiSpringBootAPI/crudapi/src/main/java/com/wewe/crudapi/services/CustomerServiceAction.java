package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceAction implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer createCustomer(Customer customer) {
        if(customer == null || customer.getName() == null) {
            throw new IllegalArgumentException("Customer data is missing");
        }
        return customerRepository.save(customer);
    }

    @Autowired
    public CustomerServiceAction(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Integer id) {
        Optional<Customer> result = customerRepository.findById(id);
        Customer data = null;
        if(result.isPresent()) {
            data = result.get();
        } else {
            throw new RuntimeException("ไม่พบข้อมูลลูกค้ารหัส " + id);
        }
        return data;
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }
}
