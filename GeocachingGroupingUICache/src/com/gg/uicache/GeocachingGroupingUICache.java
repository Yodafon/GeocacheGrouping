package com.gg.uicache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableAutoConfiguration
@ComponentScan("com.gg.uicache")
public class GeocachingGroupingUICache {

    public static void main(String[] args) {
        SpringApplication.run(GeocachingGroupingUICache.class);
    }


}
