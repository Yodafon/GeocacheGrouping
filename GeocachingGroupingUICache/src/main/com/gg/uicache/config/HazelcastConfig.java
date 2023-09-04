package com.gg.uicache.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {


    public static final String REGION_CACHE = "regionCache";
    public static final String COUNTY_CACHE = "countyCache";
    public static final String GEOCACHE_DETAILS_CACHE = "geocacheDetailsCache";

    @Bean
    public HazelcastInstance createHazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

}
