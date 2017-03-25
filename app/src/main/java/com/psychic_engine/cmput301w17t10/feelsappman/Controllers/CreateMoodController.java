package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

/**
 * Created by jyuen1 on 3/7/17.
 */

public class CreateMoodController {

    public static boolean updateMoodEventList(String moodString, String socialSettingString, String trigger, Photograph photo, String location) {
        Mood mood;
        SocialSetting socialSetting;
        switch(moodString) {
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
            default:
                return false;
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

        MoodEvent moodEvent = new MoodEvent(mood, socialSetting, trigger, photo, location);
        // Mock elastic search add
        ElasticMoodController.AddMoodEventTask addMoodEventTask = new ElasticMoodController
                .AddMoodEventTask();
        addMoodEventTask.execute(moodEvent);

        // add to participant
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        Log.i("Add", "Adding to the self participant "+ ParticipantSingleton.getInstance().getSelfParticipant().getLogin());
        participant.addMoodEvent(moodEvent);

        // update most recent mood event
        participant.setMostRecentMoodEvent(moodEvent);

        // Test
        ElasticParticipantController.UpdateParticipantTask updateParticipantTask = new ElasticParticipantController.UpdateParticipantTask();
        updateParticipantTask.execute(participant);

        return true;
    }
}
