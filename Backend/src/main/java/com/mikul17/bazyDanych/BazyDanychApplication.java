package com.mikul17.bazyDanych;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BazyDanychApplication {

	public static void main(String[] args) {
		SpringApplication.run(BazyDanychApplication.class, args);
	}
}