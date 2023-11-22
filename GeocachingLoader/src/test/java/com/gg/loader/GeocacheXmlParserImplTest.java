package com.gg.loader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gg.generated.Gpx;
import com.gg.generated.ObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
public class GeocacheXmlParserImplTest {

    @Mock
    private XmlMapper xmlMapper;

    @InjectMocks
    private GeocacheXmlParserImpl xmlParser;

    @BeforeEach
    void setUp() {
        xmlParser.setFileInputStream(mock(FileInputStream.class));
    }

    @Test
    public void parse() throws IOException {
        when(xmlMapper.readValue(any(FileInputStream.class), eq(Gpx.class))).thenReturn(createMockGpx());
        Stream<Gpx.Wpt> parse = xmlParser.parse();
        assertThat(parse.count(), is(1L));
        assertTrue(parse.isParallel());
    }

    @Test
    public void ioExceptionParse(CapturedOutput output) throws IOException {
        when(xmlMapper.readValue(any(FileInputStream.class), eq(Gpx.class))).thenThrow(new IOException());
        Stream<Gpx.Wpt> parse = xmlParser.parse();
        assertThat(output.getAll(), containsString("IO Exception"));
        assertThat(parse.count(), is(0L));
    }


    @Test
    public void generalExceptionParse(CapturedOutput output) throws IOException {
        when(xmlMapper.readValue(any(FileInputStream.class), eq(Gpx.class))).thenThrow(new ArrayIndexOutOfBoundsException());
        Stream<Gpx.Wpt> parse = xmlParser.parse();
        assertThat(output.getAll(), containsString("Error occurred"));
        assertThat(parse.count(), is(0L));
    }

    private Gpx createMockGpx() {
        ObjectFactory objectFactory = new ObjectFactory();
        Gpx gpx = objectFactory.createGpx();
        gpx.getWpt().add(objectFactory.createGpxWpt());
        return gpx;
    }
}