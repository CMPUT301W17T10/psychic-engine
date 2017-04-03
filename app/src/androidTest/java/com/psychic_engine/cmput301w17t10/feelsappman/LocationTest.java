package com.psychic_engine.cmput301w17t10.feelsappman;

import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyProfileActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Pierre Lin on 4/2/2017.
 */

public class LocationTest extends ActivityInstrumentationTestCase2 {

    public LocationTest() {
        super(MyProfileActivity.class);
    }

    public void testSetLocation() {
        MoodLocation testLoc = new MoodLocation(new GeoPoint(1.0, 2.0));
        assertTrue(testLoc != null);
    }

    public void testGetCoords() {
        MoodLocation testLoc = new MoodLocation(new GeoPoint(1.0, 2.0));
        assertTrue(testLoc.getLatitudeStr() == "1.0");
        assertTrue(testLoc.getLongitudeStr() == "2.0");
    }

}
