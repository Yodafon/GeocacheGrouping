package com.gg.uicache.incoming;


import com.gg.generated.GeocacheByCounty;
import com.gg.generated.GeocacheByRegion;
import com.gg.generated.GeocacheDetail;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
public class JMSMessageListenerTest {

    @Mock
    private HazelcastInstance hazelcastInstance;

    @Mock
    IMap<Object, Object> mapMock;

    @InjectMocks
    private JMSMessageListener jmsMessageListener;

    @Test
    public void countyListener() {
        Mockito.when(hazelcastInstance.getMap(anyString())).thenReturn(mapMock);
        jmsMessageListener.countyListener(createCounty());
        verify(mapMock).put(any(), any());
    }

    @Test
    public void regionListener() {
        Mockito.when(hazelcastInstance.getMap(anyString())).thenReturn(mapMock);
        jmsMessageListener.regionListener(createRegion());
        verify(mapMock).put(any(), any());
    }

    @Test
    public void geocacheDetailsListener() {
        Mockito.when(hazelcastInstance.getMap(anyString())).thenReturn(mapMock);
        jmsMessageListener.geocacheDetailsListener(createGeocacheDetails());
        verify(mapMock).put(any(), any());
    }


    private GeocacheByRegion createRegion() {
        GeocacheByRegion geocacheByRegion = new GeocacheByRegion();
        geocacheByRegion.setRegion("Pest");
        return geocacheByRegion;
    }

    private GeocacheDetail createGeocacheDetails() {
        GeocacheDetail geocacheDetail = new GeocacheDetail();
        geocacheDetail.setId("GC12345");
        return geocacheDetail;
    }


    private GeocacheByCounty createCounty() {
        GeocacheByCounty geocacheByCounty = new GeocacheByCounty();
        geocacheByCounty.setCounty("Pest");
        return geocacheByCounty;
    }


}