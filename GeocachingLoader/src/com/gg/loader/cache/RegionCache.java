package com.gg.loader.cache;

import com.gg.generated.Nuts.Objects.NUTSLB20214326.Geometries;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gg.loader.cache.HazelcastDevConfig.REGION_CACHE_MAP;

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

        List<Geometries> geometries = restEndpoint.getNuts().getObjects().getNUTSLB20214326().getGeometries();

        Map<String, String> regionList = geometries.stream().map(Geometries::getProperties)
                .filter(item -> "HU".equals(item.getCNTRCODE()) && 2L == item.getLEVLCODE())
                .collect(Collectors.toMap(Geometries.Properties::getNUTSID, Geometries.Properties::getNUTSNAME));


        Map<String, String> nutsMap = geometries.stream().map(Geometries::getProperties)
                .filter(item -> "HU".equals(item.getCNTRCODE()) && 3L == item.getLEVLCODE())
                .collect(Collectors.toMap(Geometries.Properties::getNUTSNAME, item -> regionList.get(item.getNUTSID().substring(0, item.getNUTSID().length() - 1))));

        cacheMap.putAll(nutsMap);
    }


    public String getByKey(String county) {
        IMap<String, String> map = hazelcastInstance.getMap(REGION_CACHE_MAP);
        return map.get(county);
    }


}
