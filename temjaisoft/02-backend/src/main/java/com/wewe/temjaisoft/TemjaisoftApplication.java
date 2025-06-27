package com.wewe.temjaisoft;

import com.wewe.temjaisoft.model.User;
import com.wewe.temjaisoft.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TemjaisoftApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemjaisoftApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder encoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User(null, "admin", encoder.encode("Good*123456"), "ADMIN");
				userRepository.save(admin);
				System.out.println("สร้างผู้ใช้เริ่มต้น: admin/Good*123456");
			}
		};
	}

}
