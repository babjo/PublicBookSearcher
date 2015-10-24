package com.seoul.publicbooksearcher.domain;

public class Location {

    public final double latitude; // 위도
    public final double longitude; // 경도

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distance(Location other) {
        double toLat = other.latitude;
        double toLon = other.longitude;
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - latitude;
        double deltaLon = toLon - longitude;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(latitude) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
