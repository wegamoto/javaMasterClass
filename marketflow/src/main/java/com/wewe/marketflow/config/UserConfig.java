package com.wewe.marketflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("admin")
                        .password(encoder.encode("@!admin123"))
                        .roles("ADMIN")
                        .build()
        );
        manager.createUser(
                User.withUsername("user")
                        .password(encoder.encode("@!user123"))
                        .roles("USER")
                        .build()
        );
        return manager;
    }
}

