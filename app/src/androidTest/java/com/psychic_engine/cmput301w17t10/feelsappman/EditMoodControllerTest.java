package com.psychic_engine.cmput301w17t10.feelsappman;

import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;

import static com.psychic_engine.cmput301w17t10.feelsappman.EditMoodController.updateMoodEventList;

/**
 * Created by adong on 3/13/17.
 */

public class EditMoodControllerTest extends TestCase {

    public void testEditMoodController () {
        Participant selfParticipant = new Participant("alex");
        String moodString = "Sad";
        String socialSettingString = "Alone";
        String trigger = "301";
        Mood mood = new Mood(MoodState.HAPPY);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        instance.addParticipant("alex");
        assertTrue(instance.setSelfParticipant(selfParticipant));
        instance.getSelfParticipant().addMoodEvent(moodEvent);
        ArrayList<MoodEvent> moodEventsRecent = ParticipantSingleton.getInstance()
                .getSelfParticipant().getMoodList();
        int position = moodEventsRecent.size() - 1;
        if (instance.getSelfParticipant().getMoodList() != null) {
            instance.getSelfParticipant().getMoodList().clear();
        }
        // test if the update was unsuccessful
        assertTrue("update unsuccessful", EditMoodController.updateMoodEventList
                   (position, moodString, socialSettingString, trigger, null, null));
        assertEquals("empty, update fail", instance.getSelfParticipant().getMoodList()
        .get(0).getMood().getMood(),
        moodEvent.getMood().getMood());
}
}
