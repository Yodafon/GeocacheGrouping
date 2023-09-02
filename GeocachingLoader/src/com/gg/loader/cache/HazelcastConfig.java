package com.gg.loader.cache;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HazelcastConfig {


  @Bean
  public HazelcastInstance hazelcastInstance() {
    Config hazel1 = new Config("hazel1");
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(hazel1);
    return hazelcastInstance;
  }

  public static final String REGION_CACHE_MAP = "regionCacheMap";

}
