package com.seoul.publicbooksearcher.domain;

public class Location {

    public final double latitude; // 위도
    public final double longitude; // 경도

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distance(Location location) {
        android.location.Location one = new android.location.Location("");
        android.location.Location other = new android.location.Location("");

        one.setLatitude(latitude);
        one.setLongitude(longitude);

        other.setLatitude(location.latitude);
        other.setLongitude(location.longitude);

        return one.distanceTo(other)/1000;
    }
}
