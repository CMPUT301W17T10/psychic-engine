package com.psychic_engine.cmput301w17t10.feelsappman;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class MoodTest extends TestCase {

    public void testGetColor() {
        Mood moodHappy = new Mood(MoodState.Happy);
        assertEquals(moodHappy.getColor(), MoodColor.Green);

        Mood moodSad = new Mood(MoodState.Sad);
        assertEquals(moodSad.getColor(), MoodColor.Blue);

        Mood moodShame = new Mood(MoodState.Shame);
        assertEquals(moodShame.getColor(), MoodColor.Purple);

        Mood moodSurprised = new Mood(MoodState.Surprised);
        assertEquals(moodSurprised.getColor(), MoodColor.Pink);

        Mood moodDisgust = new Mood(MoodState.Disgust);
        assertEquals(moodDisgust.getColor(), MoodColor.LightGreen);

        Mood moodConfused = new Mood(MoodState.Confused);
        assertEquals(moodConfused.getColor(), MoodColor.Yellow);

        Mood moodFear = new Mood(MoodState.Fear);
        assertEquals(moodFear.getColor(), MoodColor.Orange);

        Mood moodAnger = new Mood(MoodState.Anger);
        assertEquals(moodAnger.getColor(), MoodColor.Red);
    }

    public void testGetIcon() {
        Mood moodHappy = new Mood(MoodState.Happy);
        //assertEquals(moodHappy.getIcon(), "happyicon.png");

        Mood moodSad = new Mood(MoodState.Sad);
        //assertEquals(moodSad.getIcon(), "sadicon.png");

        Mood moodShame = new Mood(MoodState.Shame);
        //assertEquals(moodShame.getIcon(), "shameicon.png");

        Mood moodSurprised = new Mood(MoodState.Surprised);
        //assertEquals(moodSurprised.getIcon(), "surprisedicon.png");

        Mood moodDisgust = new Mood(MoodState.Disgust);
        assertEquals(moodDisgust.getIcon(), "disgusticon.png");

        Mood moodConfused = new Mood(MoodState.Confused);
        //assertEquals(moodConfused.getIcon(), "confusedicon.png");

        Mood moodFear = new Mood(MoodState.Fear);
        //assertEquals(moodFear.getIcon(), "fearicon.png");

        Mood moodAnger = new Mood(MoodState.Anger);
        //assertEquals(moodAnger.getIcon(), "angericon.png");
    }

    public void testGetMood() {
        Mood moodHappy = new Mood(MoodState.Happy);
        assertEquals(moodHappy.getMood(), MoodState.Happy);

        Mood moodSad = new Mood(MoodState.Sad);
        assertEquals(moodSad.getMood(), MoodState.Sad);

        Mood moodShame = new Mood(MoodState.Shame);
        assertEquals(moodShame.getMood(), MoodState.Shame);

        Mood moodSurprised = new Mood(MoodState.Surprised);
        assertEquals(moodSurprised.getMood(), MoodState.Surprised);

        Mood moodDisgust = new Mood(MoodState.Disgust);
        assertEquals(moodDisgust.getMood(), MoodState.Disgust);

        Mood moodConfused = new Mood(MoodState.Confused);
        assertEquals(moodConfused.getMood(), MoodState.Confused);

        Mood moodFear = new Mood(MoodState.Fear);
        assertEquals(moodFear.getMood(), MoodState.Fear);

        Mood moodAnger = new Mood(MoodState.Anger);
        assertEquals(moodAnger.getMood(), MoodState.Anger);
    }

    public void testSetMood() {
        Mood mood = new Mood(MoodState.Happy);
        assertEquals(mood.getMood(), MoodState.Happy);
        mood.setMood(MoodState.Anger);
        assertEquals(mood.getMood(), MoodState.Anger);
    }
}
