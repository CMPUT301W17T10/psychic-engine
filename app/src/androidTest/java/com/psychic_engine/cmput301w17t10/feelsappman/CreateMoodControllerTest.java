package com.psychic_engine.cmput301w17t10.feelsappman;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.CreateMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import junit.framework.TestCase;

import org.junit.Before;

/**
 * Created by adong on 3/13/17.
 * Commented by adong
 */

/**
 * Tests whether or not the CreateMoodController is able to editMoodEvent the participant's mood list
 * upon creation of the mood they wish to createMoodEvent in the CreateMoodActivity.
 * @see CreateMoodController
 */
public class CreateMoodControllerTest extends TestCase {

    private ParticipantSingleton instance = ParticipantSingleton.getInstance();
    private MoodEvent moodEvent;

    @Before
    public void setUp() {
        Participant testParticipant = new Participant("TestCreateMood");
        instance.addParticipant(testParticipant);
        instance.setSelfParticipant(testParticipant);
        Mood mood = new Mood(MoodState.HAPPY);
        moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
    }

    /**
     * Initialize parameters and classes for the test case. selfParticipant is required to
     * set the self participant of the instance, and the add a dummy mood event to test the
     * method.
     */
    public void testUpdateMoodEventList () {
        String moodString = "Happy";
        String socialSettingString = "Alone";
        String trigger = "301";
        try {
            if (instance.getSelfParticipant().getMoodList() != null) {
                instance.getSelfParticipant().getMoodList().clear();
            }
        }
            catch (NullPointerException e) {
                assertEquals("editMoodEvent unsuccessful", CreateMoodController.createMoodEvent
                        (moodString, socialSettingString, trigger, null, null), 0);
                assertEquals("empty, editMoodEvent fail", instance.getSelfParticipant().getMoodList()
                        .get(0).getMood().getMood(),
                        moodEvent.getMood().getMood());
            }
        // test if the editMoodEvent was unsuccessful
        assertEquals("editMoodEvent unsuccessful", CreateMoodController.createMoodEvent
                (moodString, socialSettingString, trigger, null, null), 0);
        assertEquals("empty, editMoodEvent fail", instance.getSelfParticipant().getMoodList()
                        .get(0).getMood().getMood(),
                moodEvent.getMood().getMood());
    }
}
