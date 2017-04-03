
package com.psychic_engine.cmput301w17t10.feelsappman;

import android.graphics.Bitmap;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.Date;

/**
 * Created by jyuen1 on 2/27/17.
 * Commented by adong
 */

/**
 * MoodEvent Tests will test the functionality of the MoodEvent class, to ensure the proper
 * functionality. Getters and setters with no real purpose other than returning/setting some object
 * or attribute are excluded as they are trivial.
 * @see MoodEvent
 * @see Mood
 */
public class MoodEventTest extends TestCase {

    private String trigger;
    private Mood mood;
    private String failTrigger1;
    private String failTrigger2;
    private String failTrigger3;
    private Participant moodTestUser;
    private MoodEvent moodEvent;
    private ParticipantSingleton instance = ParticipantSingleton.getInstance();

    @Before
    public void setUp() {
        //initial singleton setup to add
        moodTestUser = new Participant("MoodTester");
        instance.addParticipant(moodTestUser);
        instance.setSelfParticipant(moodTestUser);
       // moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "", null, null);
        trigger = "301";
        failTrigger1 = "a a a a";
        failTrigger2 = "aaaaaaaaaaaaaaaaaaaaa";
        failTrigger3 = "aaaaa aaaaa aaaaa aaaaa aaaaa";

        mood = new Mood(MoodState.CONFUSED);

    }

    /**
     * Setter method to determine whether the setTrigger() method will work. We test it by
     * determining whether or not the string portion is equivalent. Tests the exception where the
     * participant should be restricted to 3 words and less than 20 characters of text.
     */

    public void test1_SetTrigger() {

        try {
            moodEvent.setTrigger(trigger);
        } catch (TriggerTooLongException e) {
            assertTrue(false);
        }
        assertEquals(moodEvent.getTrigger(), trigger);

        try {
            moodEvent.setTrigger(failTrigger1);
        } catch (TriggerTooLongException e) {
            assertTrue("Invalid comment", true);
        }
        assertEquals(moodEvent.getTrigger(), trigger);

        try {
            moodEvent.setTrigger(failTrigger2);
        } catch (TriggerTooLongException e) {
            assertTrue("Invalid comment",true);
        }

        assertEquals(moodEvent.getTrigger(), trigger);

        try {
            moodEvent.setTrigger(failTrigger3);
        } catch (TriggerTooLongException e) {
            assertTrue("Invalid comment",true);
        }

        assertEquals(moodEvent.getTrigger(), trigger);

    }
}