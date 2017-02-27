package com.psychic_engine.cmput301w17t10.feelsappman;

import android.media.Image;

/**
 * Created by jyuen1 on 2/27/17.
 */

enum MoodState {
    Sad, Happy, Shame, Fear, Anger, Surprised, Disgust, Confused
};

enum MoodColor {
    Blue, Green, Purple, Orange, Red, Pink, LightGreen, Yellow
};

public class Mood {
    private MoodState state;
    private MoodColor color;
    private Image icon;

    public Mood(MoodState mood) {
        setMoodState(mood);
    }

    public MoodState getMoodState() { return state; }

    public void setMoodState(MoodState mood) {
        state = mood;
        switch(mood) {
            case Sad:
                color = MoodColor.Blue;
                //icon = "sadicon.png";
                break;
            case Happy:
                color = MoodColor.Green;
                //icon = "happyicon.png";
                break;
            case Shame:
                color = MoodColor.Purple;
                //icon = "shameicon.png";
                break;
            case Fear:
                color = MoodColor.Orange;
                //icon = "fearicon.png";
                break;
            case Anger:
                color = MoodColor.Red;
                //icon = "angericon.png";
                break;
            case Surprised:
                color = MoodColor.Pink;
                //icon = "surprisedicon.png";
                break;
            case Disgust:
                color = MoodColor.LightGreen;
                //icon = "disgusticon.png";
                break;
            case Confused:
                color = MoodColor.Yellow;
                //icon = "confusedicon.png";
                break;
        }
    }
}
