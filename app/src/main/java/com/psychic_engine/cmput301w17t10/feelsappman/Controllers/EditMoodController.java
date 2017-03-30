package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jyuen1 on 3/8/2017.
 * Comments by adong on 3/28/2017.
 */

/**
 * EditMoodController is similar to CreateMoodController in that it will update the mood event
 * list of the participant with a new mood event. Since a new date is created, it is counted as
 * the most recent mood event to be changed by the participant. Since we change the details of the
 * mood event, we would need to update the mood event in the elastic server. A change in mood event
 * for the participant also requires a participant update to the server.
 */
public class EditMoodController {

    /**
     * Parameters are obtained from the the activity where we edit the moods. All changes would be
     * applied to including the choice of adding your current location at this time.
     * @param moodEvent
     * @param moodString
     * @param socialSettingString
     * @param trigger
     * @param photo
     * @param location
     * @throws TriggerTooLongException
     */
    public static void updateMoodEventList(MoodEvent moodEvent, String moodString, String socialSettingString, String trigger, Photograph photo, MoodLocation location) throws TriggerTooLongException {

        Mood mood = null;
        SocialSetting socialSetting;

        switch (moodString) {        // TODO refactor this - inside MoodState enum class?
            case "Sad":
                mood = new Mood(MoodState.SAD);
                break;
            case "Happy":
                mood = new Mood(MoodState.HAPPY);
                break;
            case "Shame":
                mood = new Mood(MoodState.SHAME);
                break;
            case "Fear":
                mood = new Mood(MoodState.FEAR);
                break;
            case "Anger":
                mood = new Mood(MoodState.ANGER);
                break;
            case "Surprised":
                mood = new Mood(MoodState.SURPRISED);
                break;
            case "Disgust":
                mood = new Mood(MoodState.DISGUST);
                break;
            case "Confused":
                mood = new Mood(MoodState.CONFUSED);
                break;
        }

        switch (socialSettingString) {
            case "Alone":
                socialSetting = SocialSetting.ALONE;
                break;
            case "One Other":
                socialSetting = SocialSetting.ONEOTHER;
                break;
            case "Two To Several":
                socialSetting = SocialSetting.TWOTOSEVERAL;
                break;
            case "Crowd":
                socialSetting = SocialSetting.CROWD;
                break;
            default:
                socialSetting = null;
        }

        // update properties of the mood event
        moodEvent.setMood(mood);
        moodEvent.setDate(new Date());
        moodEvent.setSocialSetting(socialSetting);
        try {
            moodEvent.setTrigger(trigger);
        } catch (TriggerTooLongException e) {
            throw new TriggerTooLongException();
        }
        moodEvent.setPicture(photo);
        moodEvent.setLocation(location);

        // update the most recent mood event in elastic
        ElasticMoodController.UpdateMoodTask updateMoodTask = new ElasticMoodController.UpdateMoodTask();
        updateMoodTask.execute(moodEvent);

        // update the most recent mood event
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        participant.setMostRecentMoodEvent(moodEvent);

        // update the participant in the elastic server
        ElasticParticipantController.UpdateParticipantTask upt = new ElasticParticipantController
                .UpdateParticipantTask();
        upt.execute(participant);

    }
}
