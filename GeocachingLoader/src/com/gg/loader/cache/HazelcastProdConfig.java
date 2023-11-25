package com.gg.loader.cache;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile(value = "prod")
public class HazelcastProdConfig {


  @Bean
  public HazelcastInstance hazelcastInstance() {
    Config config = Config.load();
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
            .setProperty("namespace", "default")
            .setProperty("service-name", "loader-hazelcast-discovery-service");
    return Hazelcast.newHazelcastInstance(config);
  }

  public static final String REGION_CACHE_MAP = "regionCacheMap";

}
