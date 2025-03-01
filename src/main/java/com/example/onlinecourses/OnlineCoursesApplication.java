package com.example.onlinecourses;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OnlineCoursesApplication {

	public static void main(String[] args) {
		// Load environment variables
		Dotenv.load();
		SpringApplication.run(OnlineCoursesApplication.class, args);
	}

}