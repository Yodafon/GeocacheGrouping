package com.gg.loader;

import com.gg.generated.Gpx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
public class GeocachePublisherImplTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private GeocachePublisherImpl geocachePublisher;

    @BeforeEach
    public void setUp() throws Exception {
        geocachePublisher.setDestination("dest1");
    }

    @Test
    public void successPublish() {
        geocachePublisher.publish(mock(Gpx.Wpt.class));
        verify(jmsTemplate, times(1)).convertAndSend(eq("dest1"), any(Gpx.Wpt.class));
    }

    @Test
    public void failedPublish(CapturedOutput output) {
        doAnswer(invocation -> {
            throw new ArrayIndexOutOfBoundsException();
        }).when(jmsTemplate).convertAndSend(anyString(), any(Gpx.Wpt.class));
        geocachePublisher.publish(mock(Gpx.Wpt.class));
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(Gpx.Wpt.class));
        assertThat(output.getAll(), containsString("Current object can't be published"));

    }

}
