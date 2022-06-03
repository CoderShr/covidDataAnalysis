package com.mindtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CovidDataAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidDataAnalysisApplication.class, args);
	}

}

