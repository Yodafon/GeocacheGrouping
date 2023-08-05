package com.gg.loader;

import com.gg.generated.Gpx;

public interface GeocachePublisher {

    public void publish(Gpx.Wpt geocache);
}
