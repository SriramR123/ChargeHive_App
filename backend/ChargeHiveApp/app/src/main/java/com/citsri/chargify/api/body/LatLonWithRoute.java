package com.citsri.chargify.api.body;


import java.util.List;

public class LatLonWithRoute {
    double lat;
    double lon;

    List<EVStation> routes;

    public LatLonWithRoute(double lat, double lon, List<EVStation> routes) {
        this.lat = lat;
        this.lon = lon;
        this.routes = routes;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public List<EVStation> getRoutes() {
        return routes;
    }
}
