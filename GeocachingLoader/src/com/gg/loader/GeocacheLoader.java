package com.gg.loader;


import com.gg.generated.Gpx;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

@Component
public class GeocacheLoader {

    @Resource
    private GeocachePublisher publisher;

    @Resource
    private GeocacheXmlParser parser;

    @Scheduled(cron = "5 * * * * *")
    public void run() {
        Collection<Gpx.Wpt> geocaches = parser.parse(); // fork/join -ba rakni
        geocaches.parallelStream().forEach(publisher::publish);

    }


}

