package com.psychic_engine.cmput301w17t10.feelsappman;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by jyuen1 on 3/7/17.
 */

public class CreateMoodController {

    static boolean updateMoodEventList(String moodString, String trigger, Photograph photo, Location location) {

        Mood mood = null;

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

        String defaultTriggerMsg = "20 chars or 3 words.";
        if (trigger.equals(defaultTriggerMsg))
            trigger = "";

        MoodEvent moodEvent = new MoodEvent(mood, trigger, photo, location);

        // TODO need a class that keeps track of the current user so i can add this mood to that users mood list
        
        return true;
    }

}
