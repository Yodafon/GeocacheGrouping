package com.gg.loader;

import com.gg.generated.Gpx;

import java.util.stream.Stream;

public interface GeocacheXmlParser {

    public Stream<Gpx.Wpt> parse();
}
