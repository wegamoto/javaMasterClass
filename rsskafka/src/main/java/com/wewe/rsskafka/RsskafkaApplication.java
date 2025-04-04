package com.wewe.rsskafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RsskafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsskafkaApplication.class, args);
	}

}
