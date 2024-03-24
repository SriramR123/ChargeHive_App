package com.citsri.chargify;


import java.io.Serializable;

public class EVstationModel implements Serializable {

    String evStationName;
    String evStationLocation;
    String evStationStatus;

    double evStationRating;
    
    double lat;
    double lon;


    public EVstationModel(String evStationName, String evStationLocation, String evStationStatus, double evStationRating,double lat,double lon) {
        this.evStationName = evStationName;
        this.evStationLocation = evStationLocation;
        this.evStationStatus = evStationStatus;
        this.evStationRating = evStationRating;
        this.lat = lat;
        this.lon = lon;
    }

    public String getEvStationName() {
        return evStationName;
    }

    public String getEvStationLocation() {
        return evStationLocation;
    }

    public String getEvStationStatus() {
        return evStationStatus;
    }

    public double getEvStationRating() {
        return evStationRating;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }


    @Override
    public String toString() {
        return "EVstationModel{" +
                "evStationName='" + evStationName + '\'' +
                ", evStationLocation='" + evStationLocation + '\'' +
                ", evStationStatus='" + evStationStatus + '\'' +
                ", evStationRating=" + evStationRating +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
