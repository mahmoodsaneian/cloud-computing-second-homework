package com.example.cloud_computing_second_homework.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfiguration {
    private Long cacheTimeToLive;
    private String apiKey;
}
