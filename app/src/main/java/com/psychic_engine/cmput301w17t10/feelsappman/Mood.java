package com.psychic_engine.cmput301w17t10.feelsappman;

import android.media.Image;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class Mood {
    private MoodState mood;
    private MoodColor color;
    private Image icon;

    public Mood(MoodState mood) {
        setMood(mood);
    }

    public MoodColor getColor() { return color; }

    public Image getIcon() { return icon; }

    public MoodState getMood() { return mood; }

    public void setMood(MoodState mood) {
        this.mood = mood;
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
