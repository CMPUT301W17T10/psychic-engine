package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;


abstract class MoodController {

    static Mood selectMood(String moodString) {
        Mood mood;
        switch (moodString) {
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
                mood = null;
        }
        return mood;
    }

    static SocialSetting selectSocialSetting(String settingString) {
        SocialSetting socialSetting;
        switch (settingString) {
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
        return socialSetting;
    }

    //http://www.ssaurel.com/blog/how-to-check-if-internet-connection-is-enabled-in-android/
    // - S.Saurel
    //obtained April 1, 2017
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
