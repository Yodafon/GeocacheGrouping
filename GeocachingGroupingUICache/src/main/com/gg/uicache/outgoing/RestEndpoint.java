package com.gg.uicache.outgoing;

import com.gg.generated.GeocacheByCounty;
import com.gg.generated.GeocacheByRegion;
import com.gg.generated.GeocacheDetail;
import com.gg.uicache.config.HazelcastConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController()
@RequestMapping("/uicache")
public class RestEndpoint {


    @Autowired
    private HazelcastInstance hazelcastInstance;


    @GetMapping(value = "/regions", produces = "application/json")
    public Collection<GeocacheByRegion> getRegions() {
        IMap<String, GeocacheByRegion> map = hazelcastInstance.getMap(HazelcastConfig.REGION_CACHE);
        return map.values();
    }

    @GetMapping(value = "/counties", produces = "application/json")
    public Collection<GeocacheByCounty> getCounties() {
        IMap<String, GeocacheByCounty> map = hazelcastInstance.getMap(HazelcastConfig.COUNTY_CACHE);
        return map.values();
    }

    @GetMapping(value = "/geocachedetails", produces = "application/json")
    public Collection<GeocacheDetail> getGeocacheDetails() {
        IMap<String, GeocacheDetail> map = hazelcastInstance.getMap(HazelcastConfig.GEOCACHE_DETAILS_CACHE);
        return map.values();
    }


}
