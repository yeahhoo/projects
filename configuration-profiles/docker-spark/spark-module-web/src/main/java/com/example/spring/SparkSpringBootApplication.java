package com.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aleksandr_Savchenko
 */
@Configuration
@SpringBootApplication
public class SparkSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkSpringBootApplication.class, args);
    }
}
