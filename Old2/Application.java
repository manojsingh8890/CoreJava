package com.ibm.sec;


import org.springframework.boot.SpringApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ibm.sec"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
