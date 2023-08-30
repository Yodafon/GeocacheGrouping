package com.gg.loader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gg.generated.Gpx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

@Component
public class GeocacheXmlParserImpl implements GeocacheXmlParser {

    Logger LOGGER = LoggerFactory.getLogger(GeocacheXmlParser.class);

    @Inject
    private XmlMapper xmlMapper;

    @Value("${cache.file.location}")
    private String path;

    @Override
    public Stream<Gpx.Wpt> parse() {
        try {
            Gpx geocache = xmlMapper
                    .readValue(new FileInputStream(path), Gpx.class);
            return geocache.getWpt().parallelStream();
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        } catch (IOException e) {
            LOGGER.error("IO Exception", e);
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
        }
        return Stream.empty();
    }
}

