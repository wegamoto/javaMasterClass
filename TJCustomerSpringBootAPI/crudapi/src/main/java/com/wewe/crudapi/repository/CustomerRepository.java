package com.wewe.crudapi.repository;

import com.wewe.crudapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    // สามารถเพิ่มเมธอดค้นหาต่างๆ ตามต้องการ
    Customer findByEmail(String email);

}
