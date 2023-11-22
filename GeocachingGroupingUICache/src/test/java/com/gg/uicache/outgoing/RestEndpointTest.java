package com.gg.uicache.outgoing;


import com.gg.generated.GeocacheByCounty;
import com.gg.generated.GeocacheByRegion;
import com.gg.generated.GeocacheDetail;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class RestEndpointTest {

    @Mock
    HazelcastInstance hazelcastInstance;

    @Mock
    IMap<Object, Object> mapMock;

    @InjectMocks
    private RestEndpoint restEndpoint;

    @Test
    public void getRegions() {
        Mockito.when(hazelcastInstance.getMap(ArgumentMatchers.anyString())).thenReturn(mapMock);
        Mockito.when(mapMock.values()).thenReturn(createRegionValues());
        Collection<GeocacheByRegion> regions = restEndpoint.getRegions();
        assertThat(regions.size(), Matchers.is(1));
    }

    @Test
    public void getCountiesByRegion() {
        Mockito.when(hazelcastInstance.getMap(ArgumentMatchers.anyString())).thenReturn(mapMock);
        Mockito.when(mapMock.values()).thenReturn(createCountyValues("Pest"));
        Collection<GeocacheByCounty> regions = restEndpoint.getCountiesByRegion("Pest");
        assertThat(regions.size(), Matchers.is(1));
    }

    @Test
    public void getGeocacheDetailsByCounty() {
        Mockito.when(hazelcastInstance.getMap(ArgumentMatchers.anyString())).thenReturn(mapMock);
        Mockito.when(mapMock.values()).thenReturn(createGeocacheDetailValues("Pest"));
        Collection<GeocacheDetail> regions = restEndpoint.getGeocacheDetailsByCounty("Pest");
        assertThat(regions.size(), Matchers.is(1));
    }


    private Collection<Object> createRegionValues() {
        return List.of(new GeocacheByRegion());
    }

    private Collection<Object> createGeocacheDetailValues(String county) {
        GeocacheDetail geocacheDetail = new GeocacheDetail();
        geocacheDetail.setCounty(county);
        return List.of(geocacheDetail);
    }

    private Collection<Object> createCountyValues(String county) {
        GeocacheByCounty geocacheByCounty = new GeocacheByCounty();
        geocacheByCounty.setRegion(county);
        return List.of(geocacheByCounty);
    }

}