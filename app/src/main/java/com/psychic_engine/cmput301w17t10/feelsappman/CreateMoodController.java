package com.psychic_engine.cmput301w17t10.feelsappman;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by jyuen1 on 3/7/17.
 */

public class CreateMoodController {

    public CreateMoodController () {
    }

    public boolean updateMoodEventList(String moodString, String socialSettingString, String trigger, Photograph photo, Location location) {
        Log.d("TAG","-----------------------------------------------------");
        Mood mood;
        SocialSetting socialSetting;
        Log.d("Mood String", moodString);
        switch(moodString) {        // TODO refactor this - inside MoodState enum class?
            case "Sad": mood = new Mood(MoodState.SAD); break;
            case "Happy": mood = new Mood(MoodState.HAPPY); break;
            case "Shame": mood = new Mood(MoodState.SHAME); break;
            case "Fear": mood = new Mood(MoodState.FEAR); break;
            case "Anger": mood = new Mood(MoodState.ANGER); break;
            case "Surprised": mood = new Mood(MoodState.SURPRISED); break;
            case "Disgust": mood = new Mood(MoodState.DISGUST); break;
            case "Confused": mood = new Mood(MoodState.CONFUSED); break;
            default:
                return false;
        }

        switch(socialSettingString) {
            case "Alone": socialSetting = SocialSetting.ALONE; break;
            case "One Other": socialSetting = SocialSetting.ONEOTHER; break;
            case "Two To Several": socialSetting = SocialSetting.TWOTOSEVERAL; break;
            case "Crowd": socialSetting = SocialSetting.CROWD; break;
            default:
                socialSetting = null;
        }

        String defaultTriggerMsg = "20 chars or 3 words.";
        if (trigger.equals(defaultTriggerMsg))
            trigger = "";
        
        // TODO: objects are passed by value so have to reset the participant (this is super awkward)
        MoodEvent moodEvent = new MoodEvent(mood, socialSetting, trigger, photo, location);
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        participant.addMoodEvent(moodEvent);
        ParticipantSingleton.getInstance().setSelfParticipant(participant);
        return true;
    }

}
