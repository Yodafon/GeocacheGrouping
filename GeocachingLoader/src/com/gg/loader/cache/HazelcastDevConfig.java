package com.gg.loader.cache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile(value = "dev")
public class HazelcastDevConfig {

  @Bean
  public HazelcastInstance hazelcastInstance() {
    return Hazelcast.newHazelcastInstance();
  }

  public static final String REGION_CACHE_MAP = "regionCacheMap";

}
