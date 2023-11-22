package com.gg.uicache.incoming;

import com.gg.generated.GeocacheByCounty;
import com.gg.generated.GeocacheByRegion;
import com.gg.generated.GeocacheDetail;
import com.gg.uicache.config.HazelcastConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JMSMessageListener {

    Logger LOGGER = LoggerFactory.getLogger(JMSMessageListener.class);

    @Resource
    private HazelcastInstance hazelcastInstance;

    @JmsListener(destination = "${jms.county.input.queue}")
    public void countyListener(GeocacheByCounty message) {
        if (message.getCounty() != null) {
            IMap<String, GeocacheByCounty> map = getHazelcastInstanceMap(HazelcastConfig.COUNTY_CACHE);
            map.put(message.getCounty(), message);
            LOGGER.info("{} has been added to county cache", message.getCounty());
        }
    }

    @JmsListener(destination = "${jms.region.input.queue}")
    public void regionListener(GeocacheByRegion message) {
        if (message.getRegion() != null) {
            IMap<String, GeocacheByRegion> map = getHazelcastInstanceMap(HazelcastConfig.REGION_CACHE);
            map.put(message.getRegion(), message);
            LOGGER.info("{} has been added to region cache", message.getRegion());
        }
    }


    @JmsListener(destination = "${jms.geocachedetails.input.queue}")
    public void geocacheDetailsListener(GeocacheDetail message) {
        if (message.getId() != null) {
            IMap<String, GeocacheDetail> map = getHazelcastInstanceMap(HazelcastConfig.GEOCACHE_DETAILS_CACHE);
            map.put(message.getId(), message);
            LOGGER.info("{} has been added to geocache detail cache", message.getId());
        }
    }

    private <V, T> IMap<V, T> getHazelcastInstanceMap(String hazelcastMapName) {
        return hazelcastInstance.getMap(hazelcastMapName);
    }


}
