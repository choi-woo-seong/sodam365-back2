package com.project.sodam365;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sodam365Application {

	public static void main(String[] args) {
		SpringApplication.run(Sodam365Application.class, args);
		System.out.println("시작");
	}

}
