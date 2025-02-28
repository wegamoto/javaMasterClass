package com.wewe.crudapi.controllers;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.entity.User;
import com.wewe.crudapi.repository.CustomerRepository;
import com.wewe.crudapi.services.CustomerService;
import com.wewe.crudapi.services.EmailService;
import com.wewe.crudapi.services.LineNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*") // อนุญาตให้ Frontend เรียกใช้ API
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LineNotifyService lineNotifyService;

    @GetMapping("/customers")
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

//    @GetMapping("/customers/{id}")
//    public Customer getId(@PathVariable int id){
//        User myCustomer = customerRepository.findById(id);
//        if(myCustomer == null) {
//            throw new RuntimeException("ไม่พบข้อมูลผลูกค้ารหัส " + id);
//        }
//        return myCustomer;
//    }

    @PostMapping("/register")
    public String registerCustomer(@RequestBody Customer customer) {
        // บันทึกข้อมูลลงฐานข้อมูล
        customerRepository.save(customer);

        // ส่งอีเมลแจ้งเตือน
        String subject = "ยืนยันการลงทะเบียนติดตั้งโซล่าเซลล์";
        String body = "<h3>ขอบคุณ " + customer.getName() + " ที่สนใจติดตั้งโซล่าเซลล์!</h3>" +
                "<p>ทีมงานจะติดต่อกลับโดยเร็วที่สุด</p>";

        emailService.sendEmail(customer.getEmail(), subject, body);

//        // ส่งแจ้งเตือน LINE Notify
//        String lineMessage = "🔔 มีลูกค้าสมัครติดตั้งโซล่าเซลล์!\n" +
//                "👤 ชื่อ: " + customer.getName() + "\n" +
//                "📧 อีเมล: " + customer.getEmail() + "\n" +
//                "📞 เบอร์โทร: " + customer.getPhone() + "\n" +
//                "🏠 ที่อยู่: " + customer.getAddress();
//        lineNotifyService.sendLineNotify(lineMessage);

        return "ข้อมูลได้รับการบันทึก, อีเมลและ LINE แจ้งเตือนถูกส่งแล้ว";
    }


}

