package com.wewe.crudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication  // ควรอยู่ที่ root package
@ComponentScan(basePackages = "com.wewe.crudapi")
public class CrudapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudapiApplication.class, args);
	}

}
