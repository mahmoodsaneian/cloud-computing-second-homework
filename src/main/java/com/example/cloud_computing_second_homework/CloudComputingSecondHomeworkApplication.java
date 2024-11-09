package com.example.cloud_computing_second_homework;

import com.example.cloud_computing_second_homework.configuration.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfiguration.class)
public class CloudComputingSecondHomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudComputingSecondHomeworkApplication.class, args);
	}

}
