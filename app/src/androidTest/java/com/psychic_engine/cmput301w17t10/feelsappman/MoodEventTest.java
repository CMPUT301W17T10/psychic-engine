
package com.psychic_engine.cmput301w17t10.feelsappman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by jyuen1 on 2/27/17.
 */

/**
 * MoodEvent Tests will test the functionality of the MoodEvent class, to ensure the proper
 * setter and getter methods are working properly.
 * @see MoodEvent
 * @see Mood
 */
public class MoodEventTest extends TestCase {

    /**
     * Getter method to determine whether the getMood() method will work.
     */
    public void test1_GetMood() {
        Mood mood = new Mood(MoodState.HAPPY);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, null, null, null);
        assertEquals(mood,moodEvent.getMood());
    }

    /**
     * Setter method to determine whether the setMood() method will work.
     */
    public void test2_SetMood() {
        Mood mood = new Mood(MoodState.HAPPY);
        Mood replaceMood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, null, null, null);
        moodEvent.setMood(replaceMood);
        assertEquals(moodEvent.getMood(), replaceMood);
    }

    /**
     * Getter method to determine whether the getTrigger() method will work.
     */
    public void test3_GetTrigger() {
        Mood mood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        assertEquals(moodEvent.getTrigger(), "301");
    }

    /**
     * Setter method to determine whether the setTrigger() method will work. We test it by
     * determining whether or not the string portion is equivalent.
     */
    public void test4_SetTrigger() {
        String newTrigger = "sad dayz";
        Mood mood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        try {
            moodEvent.setTrigger(newTrigger);
        } catch (triggerTooLongException e) {
            e.printStackTrace();
        }
        assertEquals(moodEvent.getTrigger(), newTrigger);

    }

    /**
     * Getter method to determine whether the getDate() method will work. Unlikely that the date
     * function will record to the very nanosecond to fail the test.
     */
    public void test5_GetDate() {
        Mood mood = new Mood(MoodState.ANGER);
        Date date = new Date();
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        assertEquals(moodEvent.getDate(), date);
    }

    /**
     * Setter method to determine whether the setDate() method will work. The date will be set 1000
     * milliseconds ahead of the base time.
     */
    public void test6_SetDate() {
        Mood mood = new Mood(MoodState.ANGER);
        Date newDate = new Date(1000);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        moodEvent.setDate(newDate);
        assertEquals(moodEvent.getDate(), newDate);
    }

    /**
     * Getter method to determine whether the method will work. The test determines
     * if the photo is equivalent to the photo class that we created
     * @see Photograph
     */
    public void test7_GetPicture() {
        int width = 50;
        int length = 50;

        /*
        Taken from http://stackoverflow.com/questions/5663671/creating-an-empty-bitmap-and-drawing-though-canvas-in-android
        Obtained at March 13, 2017 by bigstones
         */
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(width, length, conf); // this creates a MUTABLE bitmap
        Photograph photo = new Photograph(bmp);
        Mood mood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", photo, null);
        assertEquals(moodEvent.getPicture(), photo);

    }

    /**
     * Setter method to set the picture to something that we would like. We set a bitmap to
     * a set dimension and replace it with another size. The test will determine whether the photos
     * are alike.
     */
    public void test8_SetPicture() {
        int width = 15;
        int length = 15;
        int newWidth = 10;
        int newLength = 10;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(width, length, conf); // this creates a MUTABLE bitmap
        Photograph photo = new Photograph(bmp);

        Bitmap newBMP = Bitmap.createBitmap(newWidth, newLength, conf); // this creates a MUTABLE bitmap
        Photograph newPhoto = new Photograph(newBMP);

        Mood mood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", photo, null);
        moodEvent.setPicture(newPhoto);
        assertEquals(moodEvent.getPicture(), newPhoto);
    }
    /*
    TODO: Location to be done upon implementation
    public void test9_GetLocation() {
    }
        
    public void test10_SetLocation() {
    }
    */
}


