package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

/**
 * Created by jyuen1 on 3/8/2017.
 */

public class EditMoodController {

    public static boolean updateMoodEventList(int moodEventPosition, String moodString, String socialSettingString, String trigger, Photograph photo, String location) {

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

        MoodEvent moodEvent = new MoodEvent(mood, socialSetting, trigger, photo, location);

        // replace old moodEvent with new one
        if (ParticipantSingleton.getInstance().getSelfParticipant().setMoodEvent(
                moodEventPosition, mood, socialSetting, trigger, photo, location))
            return true;
        else
            return false;

    }
}
