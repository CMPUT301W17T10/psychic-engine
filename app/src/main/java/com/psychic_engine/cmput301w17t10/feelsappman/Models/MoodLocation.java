package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Pierre Lin on 3/25/2017.
 */

public class MoodLocation {
    private GeoPoint loc;
    private double latitude;
    private double longitude;

    public MoodLocation(GeoPoint currentLocation) {
        this.loc = currentLocation;
        this.latitude = loc.getLatitude();
        this.longitude = loc.getLongitude();
    }
    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public String getLatitude() {
        return Double.toString(latitude);
    }

    public String getLongitude() {
        return Double.toString(longitude);
    }
}
