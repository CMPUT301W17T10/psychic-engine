package com.psychic_engine.cmput301w17t10.feelsappman;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class MoodTest extends TestCase {

    public void testGetColor() {
        Mood moodHappy = new Mood(MoodState.HAPPY);
        assertEquals(moodHappy.getColor(), MoodColor.GREEN);

        Mood moodSad = new Mood(MoodState.SAD);
        assertEquals(moodSad.getColor(), MoodColor.BLUE);

        Mood moodShame = new Mood(MoodState.SHAME);
        assertEquals(moodShame.getColor(), MoodColor.PURPLE);

        Mood moodSurprised = new Mood(MoodState.SURPRISED);
        assertEquals(moodSurprised.getColor(), MoodColor.PINK);

        Mood moodDisgust = new Mood(MoodState.DISGUST);
        assertEquals(moodDisgust.getColor(), MoodColor.LIGHTGREEN);

        Mood moodConfused = new Mood(MoodState.CONFUSED);
        assertEquals(moodConfused.getColor(), MoodColor.YELLOW);

        Mood moodFear = new Mood(MoodState.FEAR);
        assertEquals(moodFear.getColor(), MoodColor.ORANGE);

        Mood moodAnger = new Mood(MoodState.ANGER);
        assertEquals(moodAnger.getColor(), MoodColor.RED);
    }

    public void testGetIcon() {
        Mood moodHappy = new Mood(MoodState.HAPPY);
        //assertEquals(moodHappy.getIcon(), "happyicon.png");

        Mood moodSad = new Mood(MoodState.SAD);
        //assertEquals(moodSad.getIcon(), "sadicon.png");

        Mood moodShame = new Mood(MoodState.SHAME);
        //assertEquals(moodShame.getIcon(), "shameicon.png");

        Mood moodSurprised = new Mood(MoodState.SURPRISED);
        //assertEquals(moodSurprised.getIcon(), "surprisedicon.png");

        Mood moodDisgust = new Mood(MoodState.DISGUST);
        assertEquals(moodDisgust.getIcon(), "disgusticon.png");

        Mood moodConfused = new Mood(MoodState.CONFUSED);
        //assertEquals(moodConfused.getIcon(), "confusedicon.png");

        Mood moodFear = new Mood(MoodState.FEAR);
        //assertEquals(moodFear.getIcon(), "fearicon.png");

        Mood moodAnger = new Mood(MoodState.ANGER);
        //assertEquals(moodAnger.getIcon(), "angericon.png");
    }

    public void testGetMood() {
        Mood moodHappy = new Mood(MoodState.HAPPY);
        assertEquals(moodHappy.getMood(), MoodState.HAPPY);

        Mood moodSad = new Mood(MoodState.SAD);
        assertEquals(moodSad.getMood(), MoodState.SAD);

        Mood moodShame = new Mood(MoodState.SHAME);
        assertEquals(moodShame.getMood(), MoodState.SHAME);

        Mood moodSurprised = new Mood(MoodState.SURPRISED);
        assertEquals(moodSurprised.getMood(), MoodState.SURPRISED);

        Mood moodDisgust = new Mood(MoodState.DISGUST);
        assertEquals(moodDisgust.getMood(), MoodState.DISGUST);

        Mood moodConfused = new Mood(MoodState.CONFUSED);
        assertEquals(moodConfused.getMood(), MoodState.CONFUSED);

        Mood moodFear = new Mood(MoodState.FEAR);
        assertEquals(moodFear.getMood(), MoodState.FEAR);

        Mood moodAnger = new Mood(MoodState.ANGER);
        assertEquals(moodAnger.getMood(), MoodState.ANGER);
    }

    public void testSetMood() {
        Mood mood = new Mood(MoodState.HAPPY);
        assertEquals(mood.getMood(), MoodState.HAPPY);
        mood.setMood(MoodState.ANGER);
        assertEquals(mood.getMood(), MoodState.ANGER);
    }
}
