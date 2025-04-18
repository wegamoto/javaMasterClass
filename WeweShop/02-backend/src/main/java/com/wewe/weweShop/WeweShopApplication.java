package com.wewe.weweShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wewe.weweShop")  // กำหนดให้ Spring Scan Package นี้
public class WeweShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeweShopApplication.class, args);
	}

}
