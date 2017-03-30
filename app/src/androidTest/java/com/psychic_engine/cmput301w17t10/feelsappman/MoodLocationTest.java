package com.psychic_engine.cmput301w17t10.feelsappman;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;

import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;

/**
 * @author adong
 * Commented by adong 3/30/2017
 * Test functionality of the model class MoodLocation which is part of the MoodEvent class as well.
 * Ignoring the getter and setter methods that simply returns/sets a component in MoodLocation as
 * they are trivial. Any additional functionalities will be tested in those methods.
 * @see MoodLocation
 * @see MoodEvent
 */
public class MoodLocationTest extends TestCase {

    /**
     * Test the method if it returns a properly formatted string of the latitude
     */
    public void test1_getLatitudeStr() {
        // initialize a mood location
        double latitude = 50.0;
        double longitude = 20.0;
        GeoPoint testPoint = new GeoPoint(latitude, longitude);
        MoodLocation moodLocation = new MoodLocation(testPoint);

        // test
        assertTrue(moodLocation.getLatitudeStr().equals("50.0"));
    }

    /**
     * Test the method if it returns a properly formatted string of the longitude
     */
    public void test2_getLongitudeStr() {
        // initialize a mood location
        double latitude = 50.0;
        double longitude = 20.0;
        GeoPoint testPoint = new GeoPoint(latitude, longitude);
        MoodLocation moodLocation = new MoodLocation(testPoint);

        // test
        assertTrue(moodLocation.getLongitudeStr().equals("20.0"));
    }
}
