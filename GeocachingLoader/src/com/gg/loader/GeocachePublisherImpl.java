package com.gg.loader;

import com.gg.generated.Gpx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;

@Component
public class GeocachePublisherImpl implements GeocachePublisher {

    Logger LOGGER = LoggerFactory.getLogger(GeocachePublisher.class);

    @Resource
    private RestTemplate restTemplate;

    @Value("${geocachegroup.rest.url}")
    private String url;

    @Override
    public void publish(Gpx.Wpt geocache) {
        try {
            HttpEntity<Gpx.Wpt> geocacheHttpEntity = new HttpEntity<>(geocache);
            restTemplate.postForEntity(url, geocacheHttpEntity, Void.class, Collections.emptyMap());
        } catch (Exception e) {
            LOGGER.error("Current object can't be published {}", geocache, e);
        }
    }
}
