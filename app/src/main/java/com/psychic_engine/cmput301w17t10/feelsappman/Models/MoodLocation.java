package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by Pierre Lin on 3/25/2017.
 * Comments by adong on 3/29/2017
 */

/**
 * Model class for the location portion of the mood event if the participant chooses to add their
 * location.
 */
public class MoodLocation implements Serializable {
    private GeoPoint loc;
    private double latitude;
    private double longitude;

    /**
     * Constructor method utilizes the latitude and longitude that was previously set in the emulator.
     * Unless custom coordinates were given
     * @param currentLocation
     */
    public MoodLocation(GeoPoint currentLocation) {
        this.loc = currentLocation;
        this.latitude = loc.getLatitude();
        this.longitude = loc.getLongitude();
    }

    /**
     * Setter method to set the GeoPoint of the mood event.
     * @param loc
     */
    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    /**
     * Getter method to return the GeoPoint of the mood event.
     * @return GeoPoint of the mood event
     */
    public GeoPoint getLoc() {
        return loc;
    }

    /**
     * Getter method to return the latitude of the GeoPoint in String format.
     * @return Latitude in String format
     */
    //getters for lat long are strings because the only time we need them
    public String getLatitudeStr() {
        String str = String.format("%.4f   ", latitude);
        return str;
    }

    /**
     * Getter method to return the latitude of the GeoPoint in String format.
     * @return Longitude in String format
     */
    public String getLongitudeStr() {
        String str = String.format("%.4f", longitude);
        return str;
    }



}
