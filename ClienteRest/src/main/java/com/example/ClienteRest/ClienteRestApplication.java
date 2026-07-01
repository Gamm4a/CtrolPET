package com.example.ClienteRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.ClienteRest", "com.example.ctrolpet"})
public class ClienteRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteRestApplication.class, args);
	}

}
