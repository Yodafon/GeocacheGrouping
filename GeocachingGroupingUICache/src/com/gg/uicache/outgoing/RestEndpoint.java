package com.gg.uicache.outgoing;

import com.gg.generated.GeocacheByCounty;
import com.gg.generated.GeocacheByRegion;
import com.gg.generated.GeocacheDetail;
import com.gg.uicache.config.HazelcastConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController()
@RequestMapping("/uicache")
@CrossOrigin
public class RestEndpoint {


    @Autowired
    private HazelcastInstance hazelcastInstance;


    @GetMapping(value = "/regions", produces = "application/json")
    public Collection<GeocacheByRegion> getRegions() {
        IMap<String, GeocacheByRegion> map = hazelcastInstance.getMap(HazelcastConfig.REGION_CACHE);
        return map.values();
    }

    @GetMapping(value = "/counties/{region}", produces = "application/json")
    public Collection<GeocacheByCounty> getCountiesByRegion(@PathVariable String region) {
        IMap<String, GeocacheByCounty> map = hazelcastInstance.getMap(HazelcastConfig.COUNTY_CACHE);
        return map.values().stream().filter(item -> item.getRegion().equals(region)).toList();
    }

    @GetMapping(value = "/geocachedetails/{county}", produces = "application/json")
    public Collection<GeocacheDetail> getGeocacheDetailsByCounty(@PathVariable String county) {
        IMap<String, GeocacheDetail> map = hazelcastInstance.getMap(HazelcastConfig.GEOCACHE_DETAILS_CACHE);
        return map.values().stream().filter(item -> item.getCounty().equals(county)).toList();
    }


}
