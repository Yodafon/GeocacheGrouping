package com.gg.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan("com.gg.loader")
@SpringBootApplication
public class GeocachingGroupingLoader {
    public static void main(String[] args) {
        SpringApplication.run(GeocachingGroupingLoader.class, args);
    }
}
