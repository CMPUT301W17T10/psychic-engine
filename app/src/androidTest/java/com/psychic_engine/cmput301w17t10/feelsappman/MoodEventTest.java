package com.psychic_engine.cmput301w17t10.feelsappman;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class MoodEventTest extends TestCase {

    public void testGetId() {
        MoodEvent moodEvent = new MoodEvent(new Mood(MoodState.Happy));
        moodEvent.setId(1);
        assertEquals(moodEvent.getId(), 1);
    }

    public void testGetMood() {
        Mood mood = new Mood(MoodState.Happy);
        MoodEvent moodEvent = new MoodEvent(new Mood(MoodState.Happy));
        assertEquals(moodEvent.getMood(), mood);
    }

    public void testGetTrigger() {
        MoodEvent moodEvent = new MoodEvent(new Mood(MoodState.Happy));
        try {
            moodEvent.setTrigger("So happy");
            assertEquals(moodEvent.getTrigger(), "So happy");
        } catch (triggerTooLongException e){
            e.printStackTrace();
        }
    }

    public void testGetDate() {
        MoodEvent moodEvent = new MoodEvent(new Mood(MoodState.Happy));
        //Date date = new Date(2017, 02, 28);
        //moodEvent.setDate(date);
        //assertTrue(equals(moodEvent.getDate(), date));
    }

    public void testGetPicture() {
        MoodEvent moodEvent = new MoodEvent(new Mood(MoodState.Happy));
        //Photograph picture = new Picture( ... )
        //moodEvent.setPicture(picture);
        //assertEquals(equals(moodEvent.getPicture(), picture));
    }

    public void testGetLocation() {
        MoodEvent moodEvent = new MoodEvent(new Mood(MoodState.Happy));
        //Location location = new Location( ... )
        //moodEvent.setLocation(location);
        //assertEquals(equals(moodEvent.getLocation(), location));
    }
}
