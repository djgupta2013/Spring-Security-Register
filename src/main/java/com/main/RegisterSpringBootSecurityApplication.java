package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RegisterSpringBootSecurityApplication {
	
	
	public static void main(String[] args) {		
		
		SpringApplication.run(RegisterSpringBootSecurityApplication.class, args);
	}
}
