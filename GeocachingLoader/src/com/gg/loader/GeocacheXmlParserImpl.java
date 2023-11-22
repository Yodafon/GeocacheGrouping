package com.gg.loader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gg.generated.Gpx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

@Component
public class GeocacheXmlParserImpl implements GeocacheXmlParser {

    Logger LOGGER = LoggerFactory.getLogger(GeocacheXmlParser.class);

    @Inject
    private XmlMapper xmlMapper;

    @Resource
    private InputStream gpxInputStream;

    @Override
    public Stream<Gpx.Wpt> parse() {
        try (InputStream fis = getFileInputStream()) {
            Gpx geocache = xmlMapper.readValue(fis, Gpx.class);
            return geocache.getWpt().parallelStream();
        } catch (IOException e) {
            LOGGER.error("IO Exception", e);
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
        }
        return Stream.empty();
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        this.gpxInputStream = fileInputStream;
    }

    private InputStream getFileInputStream() {
        return gpxInputStream;
    }

    public void setXmlMapper(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }
}

