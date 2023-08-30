package com.gg.uicache.incoming;

import com.gg.generated.GeocacheByCounty;
import com.gg.generated.GeocacheByRegion;
import com.gg.generated.GeocacheDetail;
import com.gg.uicache.config.HazelcastConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JMSMessageListener {


    @Resource
    private HazelcastInstance hazelcastInstance;

    @JmsListener(destination = "${jms.county.input.queue}")
    public void countyListener(GeocacheByCounty message) {
        if (message.getRegion() != null) {
            IMap<String, GeocacheByCounty> map = hazelcastInstance.getMap(HazelcastConfig.COUNTY_CACHE);
            map.put(message.getCounty(), message);
        }
    }

    @JmsListener(destination = "${jms.region.input.queue}")
    public void regionListener(GeocacheByRegion message) {
        if (message.getRegion() != null) {
            IMap<String, GeocacheByRegion> map = hazelcastInstance.getMap(HazelcastConfig.REGION_CACHE);
            map.put(message.getRegion(), message);
        }
    }


    @JmsListener(destination = "${jms.geocachedetails.input.queue}")
    public void geocacheDetailsListener(GeocacheDetail message) {
        if (message.getRegion() != null) {
            IMap<String, GeocacheDetail> map = hazelcastInstance.getMap(HazelcastConfig.GEOCACHE_DETAILS_CACHE);
            map.put(message.getId(), message);
        }
    }


}
