package com.wewe.crudapi.controllers;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.exception.ResourceNotFoundException;
import com.wewe.crudapi.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer){
        customer.setId(0);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomer() {
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
    public Customer getCustomer(@PathVariable String email){
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
        return "ลบข้อมูลผลูกค้ารหัส " + id + " เรียบร้อยแล้ว";
    }

    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer customer){
        customer.setUpdatedAt(LocalDateTime.now());
        return customerService.saveCustomer(customer);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        Optional<Customer> existingCustomer = Optional.ofNullable(customerService.findById(id));

        if (existingCustomer.isEmpty()) {
            return ResponseEntity.notFound().build(); // หากไม่มีข้อมูลลูกค้าในระบบ ส่ง 404
        }

        Customer updatedCustomer = existingCustomer.get();

        updatedCustomer.setFirstname(customer.getFirstname());  // อัปเดตเฉพาะฟิลด์ที่ต้องการ
        updatedCustomer.setLastname(customer.getLastname());
        updatedCustomer.setEmail(customer.getEmail());
        updatedCustomer.setAddress(customer.getAddress());
        updatedCustomer.setUpdatedAt(LocalDateTime.now());

        Customer savedCustomer = customerService.saveCustomer(updatedCustomer);
        return ResponseEntity.ok(savedCustomer);
    }


}
