package com.gg.uicache.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {


    public static final String REGION_CACHE = "regionCache";
    public static final String COUNTY_CACHE = "countyCache";
    public static final String GEOCACHE_DETAILS_CACHE = "geocacheDetailsCache";
    @Value("${hazelcast.cache.timetolive.seconds}")
    private int timeToLiveSeconds;

    @Bean
    public HazelcastInstance createHazelcastInstance() {
        Config config = Config.load();
        config.getMapConfigs().values().forEach(item -> item.setTimeToLiveSeconds(timeToLiveSeconds));
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                .setProperty("namespace", "default")
                .setProperty("service-name", "uicache-hazelcast-discovery-service");
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        return hazelcastInstance;
    }

}
