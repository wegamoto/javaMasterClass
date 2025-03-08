package com.wewe.targetcustomer.services;

import com.wewe.targetcustomer.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer save(Customer customer);

    List<Customer> findAll();

    Customer findById(Integer id);

    void deleteById(Integer id);
}
