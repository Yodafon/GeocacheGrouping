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
    Config config = Config.load();
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
            .setProperty("namespace", "default")
            .setProperty("service-name", "loader-hazelcast-discovery-service");
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    return hazelcastInstance;
  }

  public static final String REGION_CACHE_MAP = "regionCacheMap";

}
