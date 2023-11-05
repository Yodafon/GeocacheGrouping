package com.gg.loader;

import com.gg.generated.Gpx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GeocachePublisherImplTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private GeocachePublisherImpl geocachePublisher;

    @Before
    public void setUp() throws Exception {
        geocachePublisher.setDestination("dest1");
    }

    @Test
    public void testPublish() {
        geocachePublisher.publish(mock(Gpx.Wpt.class));
        verify(jmsTemplate, times(1)).convertAndSend(eq("dest1"), any(Gpx.Wpt.class));
    }

}
