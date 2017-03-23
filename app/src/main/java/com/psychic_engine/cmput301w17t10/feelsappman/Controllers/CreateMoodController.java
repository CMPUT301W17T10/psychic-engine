package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        participant.addMoodEvent(moodEvent);

        // Mock elastic search add
        ElasticMoodController.AddMoodEventTask addMoodEventTask = new ElasticMoodController
                .AddMoodEventTask();
        addMoodEventTask.execute(moodEvent);

        // mock elastic search
        ElasticMoodController.FindMoodByReasonTask findMoodByReasonTask = new ElasticMoodController.FindMoodByReasonTask();
        try {
            ArrayList<MoodEvent> foundEvents = findMoodByReasonTask.execute("dead").get();
            for (MoodEvent event : foundEvents) {
                Log.i("Info", "Event has the reason: dead and mood " + event.getMood().getMood());
            }
        } catch (Exception e) {
            Log.i("Error", "Elastic server error");
        }
        // update most recent mood event
        participant.setMostRecentMoodEvent(moodEvent);

        return true;
    }
}
