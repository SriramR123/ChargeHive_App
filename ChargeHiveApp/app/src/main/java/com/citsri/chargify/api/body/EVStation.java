package com.citsri.chargify.api.body;

import java.io.Serializable;

public class EVStation implements Serializable {
    int evStationId;

    String evStationName;

    String svStationAddress;

    float rating;

    String status;

    double lat;

    double lon;


    public EVStation(int evStationId, String evStationName, String svStationAddress, float rating, String status, double lat, double lon) {
        this.evStationId = evStationId;
        this.evStationName = evStationName;
        this.svStationAddress = svStationAddress;
        this.rating = rating;
        this.status = status;
        this.lat = lat;
        this.lon = lon;
    }

    public int getEvStationId() {
        return evStationId;
    }

    public String getEvStationName() {
        return evStationName;
    }

    public String getSvStationAddress() {
        return svStationAddress;
    }

    public float getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
