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
            case SAD:
                color = MoodColor.BLUE;
                //icon = "sadicon.png";
                break;
            case HAPPY:
                color = MoodColor.GREEN;
                //icon = "happyicon.png";
                break;
            case SHAME:
                color = MoodColor.PURPLE;
                //icon = "shameicon.png";
                break;
            case FEAR:
                color = MoodColor.ORANGE;
                //icon = "fearicon.png";
                break;
            case ANGER:
                color = MoodColor.RED;
                //icon = "angericon.png";
                break;
            case SURPRISED:
                color = MoodColor.PINK;
                //icon = "surprisedicon.png";
                break;
            case DISGUST:
                color = MoodColor.LIGHTGREEN;
                //icon = "disgusticon.png";
                break;
            case CONFUSED:
                color = MoodColor.YELLOW;
                //icon = "confusedicon.png";
                break;
            default:
                color = null;
                icon = null;
        }
    }
}
