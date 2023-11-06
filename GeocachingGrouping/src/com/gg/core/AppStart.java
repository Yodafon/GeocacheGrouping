package com.gg.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AppStart {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppStart.class);

    public static void main(String[] args) {
        System.out.println("Starting Grouping application");
        SpringApplication.run(AppStart.class, args);
    }

}

