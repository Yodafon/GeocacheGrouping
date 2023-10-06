package com.gg.loader;


import com.gg.loader.cache.RegionCache;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@DependsOn("regionCache")
public class GeocacheLoader {

    @Resource
    private GeocachePublisher publisher;

    @Resource
    private GeocacheXmlParser parser;

    @Resource
    private RegionCache regionCache;

    @Scheduled(cron = "0 0 0 * * *") //daily midnight run
    @PostConstruct
    public void run() {
        parser.parse()
                .peek(wpt -> {
                    var cache = wpt.getCache();
                    if (cache.getCountry().matches("Csongrád.*")) {
                        cache.setCountry(cache.getCountry().split("-")[0]);
                    }
                }) //transform "Csongrad-Csanad" to "Csongrad"
                .peek(item -> item.getCache().setState(regionCache.getByKey(item.getCache().getCountry())))
                .forEach(publisher::publish);

    }


}

