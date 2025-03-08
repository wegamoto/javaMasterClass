package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer saveCustomer(Customer customer);

    List<Customer> findAll();

    Customer findById(Integer id);

    Customer findByEmail(String email);

    void deleteById(Integer id);
}
