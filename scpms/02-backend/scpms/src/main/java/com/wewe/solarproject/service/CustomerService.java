package com.wewe.solarproject.service;

import com.wewe.solarproject.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CustomerService {
    Page<Customer> getAllCustomers(String name, int page, int size);
    Optional<Customer> getCustomerById(Long id);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}

