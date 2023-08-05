package com.gg.loader;

import com.gg.generated.Gpx;

import java.util.Collection;

public interface GeocacheXmlParser {

    public Collection<Gpx.Wpt> parse();
}
