package com.psychic_engine.cmput301w17t10.feelsappman;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.CreateMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import junit.framework.TestCase;

/**
 * Created by adong on 3/13/17.
 * Commented by adong
 */

/**
 * Tests whether or not the CreateMoodController is able to update the participant's mood list
 * upon creation of the mood they wish to create in the CreateMoodActivity.
 * @see CreateMoodController
 */
public class CreateMoodControllerTest extends TestCase {

    /**
     * Initialize parameters and classes for the test case. selfParticipant is required to
     * set the self participant of the instance, and the add a dummy mood event to test the
     * method.
     */
    public void testUpdateMoodEventList () {
        Participant selfParticipant = new Participant("alex");
        String moodString = "Happy";
        String socialSettingString = "Alone";
        String trigger = "301";
        Mood mood = new Mood(MoodState.HAPPY);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        //instance.addParticipant("alex");
        Log.d("TAG","selfParticipant | " + selfParticipant.getLogin());
        assertTrue(instance.setSelfParticipant(selfParticipant));

        /**
         * Try and catch to determine whether or not the self participants list is empty or not. If it
         * is empty, we can go on with the test case. If it is not empty, the list will be cleared and
         * the test case will then resume.
         */
        try {
            if (instance.getSelfParticipant().getMoodList() != null) {
                instance.getSelfParticipant().getMoodList().clear();
            }
        }
            catch (NullPointerException e) {
                //assertTrue("update unsuccessful", CreateMoodController.updateMoodEventList
                        //(moodString, socialSettingString, trigger, null, null));
                assertEquals("empty, update fail", instance.getSelfParticipant().getMoodList()
                        .get(0).getMood().getMood(),
                        moodEvent.getMood().getMood());
            }
        // test if the update was unsuccessful
        //assertTrue("update unsuccessful", CreateMoodController.updateMoodEventList
                //(moodString, socialSettingString, trigger, null, null));
        assertEquals("empty, update fail", instance.getSelfParticipant().getMoodList()
                        .get(0).getMood().getMood(),
                moodEvent.getMood().getMood());
    }
}
