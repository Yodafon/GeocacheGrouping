package com.gg.loader.cache;

import com.gg.generated.Nuts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gg.loader.cache.HazelcastConfig.REGION_CACHE_MAP;

@Component
@DependsOn("hazelcastInstance")
public class RegionCache {

    @Resource
    private HazelcastInstance hazelcastInstance;

    @Resource
    private RestEndpoint restEndpoint;

    @PostConstruct
    public void populate() {
        Map<String, String> cacheMap = hazelcastInstance.getMap(REGION_CACHE_MAP);
        Nuts nuts = restEndpoint.getNuts();
        Map<String, String> regionList = nuts.getObjects().getNUTSLB20214326().getGeometries().stream().map(item -> item.getProperties())
                .filter(item -> "HU".equals(item.getCNTRCODE()) && 2L == item.getLEVLCODE())
                .collect(Collectors.toMap(Nuts.Objects.NUTSLB20214326.Geometries.Properties::getNUTSID, Nuts.Objects.NUTSLB20214326.Geometries.Properties::getNUTSNAME));


        Map<String, String> nutsMap = nuts.getObjects().getNUTSLB20214326().getGeometries().stream().map(item -> item.getProperties())
                .filter(item -> "HU".equals(item.getCNTRCODE()) && 3L == item.getLEVLCODE())
                .collect(Collectors.toMap(Nuts.Objects.NUTSLB20214326.Geometries.Properties::getNUTSNAME, item -> regionList.get(item.getNUTSID().substring(0, item.getNUTSID().length() - 1))));

        cacheMap.putAll(nutsMap);
    }


    public String getByKey(String county) {
        IMap<String, String> map = hazelcastInstance.getMap(REGION_CACHE_MAP);
        return map.get(county);
    }


}
