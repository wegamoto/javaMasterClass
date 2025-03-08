package com.wewe.targetcustomer.controllers;

import com.wewe.exception.ResourceNotFoundException;
import com.wewe.targetcustomer.entity.Customer;
import com.wewe.targetcustomer.services.CustomerServiceAction;
import com.wewe.targetcustomer.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerServiceAction customerService;

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer) {
        customer.setId(0);
        Customer savedCustomer = customerService.saveCustomer(customer);
        emailService.sendConfirmationEmail(customer.getEmail());
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/customer")
    public List<Customer> getAllUser() {
        return customerService.findAll();
    }

    @GetMapping("/customer/{email}")
    public Customer getCustomerByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }


    @Autowired
    private EmailService emailService;

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable Integer id, @RequestBody Customer customerDetails) {
        Customer existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer != null) {
            existingCustomer.setName(customerDetails.getName());
            existingCustomer.setEmail(customerDetails.getEmail());
            existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
            existingCustomer.setAddress(customerDetails.getAddress());
            return customerService.saveCustomer(existingCustomer);
        }
        throw new ResourceNotFoundException("Customer not found with id " + id);
    }


}

