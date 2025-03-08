package com.wewe.crudapi.config;

import com.wewe.crudapi.entity.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfig {

    @Bean
    public Customer customer() {
        return new Customer();
    }
}
