package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceAction implements CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceAction(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
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
    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }
}
