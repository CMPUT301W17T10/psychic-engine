package com.psychic_engine.cmput301w17t10.feelsappman;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by adong on 3/13/17.
 */

/**
 * Tests whether or not the CreateMoodController is able to update the participant's mood list
 * upon creation of the mood they wish to create in the CreateMoodActivity.
 * @see CreateMoodActivity
 */
public class CreateMoodControllerTest extends TestCase {

    public void testUpdateMoodEventList () {
        Participant selfParticipant = new Participant("alex");
        String moodString = "Happy";
        String socialSettingString = "Alone";
        String trigger = "301";
        Mood mood = new Mood(MoodState.HAPPY);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        instance.addParticipant("alex");
        Log.d("TAG","selfParticipant | " + selfParticipant.getLogin());
        assertTrue(instance.setSelfParticipant(selfParticipant));
        try {
            if (instance.getSelfParticipant().getMoodList() != null) {
                instance.getSelfParticipant().getMoodList().clear();
            }
        }
            catch (NullPointerException e) {
                assertTrue("update unsuccessful", CreateMoodController.updateMoodEventList
                        (moodString, socialSettingString, trigger, null, null));
                assertEquals("empty, update fail", instance.getSelfParticipant().getMoodList()
                        .get(0).getMood().getMood(),
                        moodEvent.getMood().getMood());
            }
        // test if the update was unsuccessful
        assertTrue("update unsuccessful", CreateMoodController.updateMoodEventList
                (moodString, socialSettingString, trigger, null, null));
        assertEquals("empty, update fail", instance.getSelfParticipant().getMoodList()
                        .get(0).getMood().getMood(),
                moodEvent.getMood().getMood());
    }
}
