package com.wewe.temjaimusic;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TemjaimusicApplication {

	public static void main(String[] args) {
		// โหลด .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// ตั้งค่าเข้า System properties เพื่อให้ Spring ใช้ได้กับ ${...}
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(TemjaimusicApplication.class, args);
	}

}
