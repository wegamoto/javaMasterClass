package com.wewe.crudapi.repository;

import com.wewe.crudapi.entity.Customer;
import com.wewe.crudapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    User findById(int id);
}
