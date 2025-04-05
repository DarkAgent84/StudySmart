package com.studysmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class StudyPlannerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyPlannerServiceApplication.class, args);
	}

}
