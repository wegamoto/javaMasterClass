package com.wewe.temjaiShop;

import com.wewe.temjaiShop.model.Role;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = "com.wewe.temjaiShop")  // กำหนดให้ Spring Scan Package นี้
//@EnableRetry
public class TemjaiShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemjaiShopApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			// เช็คว่ามี role แล้วหรือยัง เพื่อไม่ให้เพิ่มซ้ำ
			if (roleRepository.count() == 0) {
				roleRepository.save(new Role(RoleName.ROLE_USER));
				roleRepository.save(new Role(RoleName.ROLE_ADMIN));
				System.out.println("✅ Roles created");
			}
		};
	}
}
