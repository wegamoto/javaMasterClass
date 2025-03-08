package com.wewe.crudapi.controllers;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.exception.ResourceNotFoundException;
import com.wewe.crudapi.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // อนุญาตทุกโดเมน (สามารถกำหนดเฉพาะได้)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

//    @PostMapping
//    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
//        customer.setId(0);
//        Customer savedCustomer = customerService.saveCustomer(customer);
//        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
//    }

    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer){
        customer.setId(0);
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/customers")
    public List<Customer> getAllUser() {
        return customerService.findAll();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable int id){
        Customer myCustomer = customerService.findById(id);
        if(myCustomer == null) {
            throw new RuntimeException("ไม่พบข้อมูลลูกค้ารหัส " + id);
        }
        return myCustomer;
    }

    @GetMapping("/customers/{email}")
    public Customer getCustomerByEmail(@PathVariable String email) {
        Customer myCustomer = customerService.findByEmail(email);
        if(myCustomer == null) {
            throw new RuntimeException("ไม่พบข้อมูลลูกค้าอีเมล์ " + email);
        }
        return myCustomer;
    }

    @DeleteMapping("/customers/{id}")
    public String deleteCustomer(@PathVariable int id) {
        Customer myCustomer = customerService.findById(id);

        if(myCustomer == null) {
            throw new RuntimeException("ไม่พบข้อมูลลูกค้ารหัส " + id);
        }
        customerService.deleteById(id);
        return "ลบข้อมูลลูกค้ารหัส" + id + "เรียบร้อยแล้ว";
    }

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable Integer id, @RequestBody Customer customerDetails) {
        Customer existingCustomer = customerService.findById(id);
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
