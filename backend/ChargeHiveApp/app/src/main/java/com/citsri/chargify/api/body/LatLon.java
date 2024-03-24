package com.citsri.chargify.api.body;




public class LatLon {
    double lattitude;
    double longitude;

    public LatLon(double lattitude, double longitude) {
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
