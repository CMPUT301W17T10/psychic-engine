package com.psychic_engine.cmput301w17t10.feelsappman;

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodColor;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;

import junit.framework.TestCase;

/**
 * Created by jyuen1 on 2/27/17.
 * Commented by Alex Dong on 03/12/17
 */

/**
 * Unit tests to determine the functionality of the methods that were created in the Mood model.
 * @see Mood
 * @see MoodState
 *
 */

public class MoodTest extends TestCase {

    /**
     * Test the getter method to make sure that the colours that are obtained with their corresponding
     * emotions are correct.
     */
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
        //assertEquals(moodDisgust.getColor(), MoodColor.LIGHTGREEN);

        Mood moodConfused = new Mood(MoodState.CONFUSED);
        assertEquals(moodConfused.getColor(), MoodColor.YELLOW);

        Mood moodFear = new Mood(MoodState.FEAR);
        assertEquals(moodFear.getColor(), MoodColor.ORANGE);

        Mood moodAnger = new Mood(MoodState.ANGER);
        assertEquals(moodAnger.getColor(), MoodColor.RED);
    }

    /**
     * Test the getter method to make sure that the icons that are obtained with their corresponding
     * emotions are correct.
     */
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
        //sassertEquals(moodDisgust.getIcon(), "disgusticon.png");

        Mood moodConfused = new Mood(MoodState.CONFUSED);
        //assertEquals(moodConfused.getIcon(), "confusedicon.png");

        Mood moodFear = new Mood(MoodState.FEAR);
        //assertEquals(moodFear.getIcon(), "fearicon.png");

        Mood moodAnger = new Mood(MoodState.ANGER);
        //assertEquals(moodAnger.getIcon(), "angericon.png");
    }

    /**
     * Test getter method to make sure that the actual mood that is obtained corresponds to the
     * emotions that a participant may enter
     */
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

    /**
     * Test the setter method to make sure that the mood that the participant wants to edit to is
     * correct.
     */

    public void testSetMood() {
        Mood mood = new Mood(MoodState.HAPPY);
        assertEquals(mood.getMood(), MoodState.HAPPY);
        mood.setMood(MoodState.ANGER);
        assertEquals(mood.getMood(), MoodState.ANGER);
    }


}
