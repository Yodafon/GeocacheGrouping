package com.gg.loader;

import com.gg.generated.Gpx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class GeocachePublisherImpl implements GeocachePublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${ibm.mq.destination}")
    private String destination;

    private Logger LOGGER = LoggerFactory.getLogger(GeocachePublisherImpl.class);

    @Override
    public void publish(Gpx.Wpt geocache) {
        try {
            jmsTemplate.convertAndSend(destination, geocache);
            LOGGER.info("Geocache was sent: {}", geocache);
        } catch (Exception e) {
            LOGGER.error("Current object can't be published {}", geocache, e);
        }
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
