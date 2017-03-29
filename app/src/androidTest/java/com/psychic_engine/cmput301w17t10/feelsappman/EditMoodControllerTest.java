package com.psychic_engine.cmput301w17t10.feelsappman;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.EditMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by adong on 3/13/17.
 */

/**
 * Test case for updating the mood event of your choice in the EditMoodController. We assume that
 * the first moodEvent that is added will be converted to the new mood event settings that we set.
 * Usually, the moodEvent that will be edited.
 * @see EditMoodController
 */
public class EditMoodControllerTest extends TestCase {

    public void testEditMoodController () {
        //Initialize parameters used in the update method.
        Participant selfParticipant = new Participant("alex");
        String moodString = "Sad";
        String socialSettingString = "Alone";
        String trigger = "301";
        Mood mood = new Mood(MoodState.HAPPY);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        //instance.addParticipant("alex");

        // Test if setSelfParticipant method is done.
        assertTrue(instance.setSelfParticipant(selfParticipant));
        instance.getSelfParticipant().addMoodEvent(moodEvent);

        // get the position of the mood event to be updated (usually clicked on)
        ArrayList<MoodEvent> moodEventsRecent = ParticipantSingleton.getInstance()
                .getSelfParticipant().getMoodList();
        int position = moodEventsRecent.size() - 1;

        // test if the update was unsuccessful
        //assertTrue("update unsuccessful", EditMoodController.updateMoodEventList
          //         (position, moodString, socialSettingString, trigger, null, null));
        assertEquals("empty, update fail", instance.getSelfParticipant().getMoodList()
        .get(0).getMood().getMood(),
        MoodState.SAD);
}
}
