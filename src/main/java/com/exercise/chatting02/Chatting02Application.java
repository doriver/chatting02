package com.exercise.chatting02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication// (exclude = {SecurityAutoConfiguration.class}) Spring Security 비활성화
public class Chatting02Application {

	public static void main(String[] args) {
		SpringApplication.run(Chatting02Application.class, args);
	}

}
