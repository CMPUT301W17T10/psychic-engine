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

    //getters for lat long are strings because the only time we need them
    //
    public String getLatitudeStr() {
        String str = String.format("%.4f   ", latitude);
        return str;
    }

    public String getLongitudeStr() {
        String str = String.format("%.4f", longitude);
        return str;
    }


}
