package com.gg.loader;


import com.gg.generated.Cache;
import com.gg.generated.Gpx;
import com.gg.generated.ObjectFactory;
import com.gg.loader.cache.RegionCache;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeocacheLoaderTest {


    @Mock
    private GeocacheXmlParser parser;

    @Mock
    private GeocachePublisher publisher;

    @Mock
    private RegionCache regionCache;

    @InjectMocks
    private GeocacheLoader geocacheLoader;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void successRun() {
        when(parser.parse()).thenReturn(createStream("Pest"));
        when(regionCache.getByKey(anyString())).thenReturn("Pest");
        geocacheLoader.run();
        verify(publisher, times(1)).publish(any());

    }

    @Test
    public void csongradSpecificSuccessRun() {
        when(parser.parse()).thenReturn(createStream("Csongrád-Csanád"));
        when(regionCache.getByKey(anyString())).thenReturn("Csongrád");
        geocacheLoader.run();
        ArgumentCaptor<Gpx.Wpt> wptCaptor = ArgumentCaptor.forClass(Gpx.Wpt.class);
        verify(publisher, times(1)).publish(wptCaptor.capture());
        MatcherAssert.assertThat(wptCaptor.getValue().getCache().getCountry(), is("Csongrád"));

    }

    private Stream<Gpx.Wpt> createStream(String country) {
        return Stream.of(createWptWithCountry(country));
    }

    private Gpx.Wpt createWptWithCountry(String country) {
        ObjectFactory objectFactory = new ObjectFactory();
        Gpx.Wpt gpxWpt = objectFactory.createGpxWpt();
        Cache cache = objectFactory.createCache();
        cache.setCountry(country);
        gpxWpt.setCache(cache);

        return gpxWpt;
    }
}