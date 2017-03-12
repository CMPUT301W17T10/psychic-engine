package com.psychic_engine.cmput301w17t10.feelsappman;

import android.media.Image;

/**
 * Created by jyuen1 on 2/27/17.
 */

/**
 * Mood model that contains the variety of emotions and their respective colours. They also
 * have their own icons that represents the emotion such as (:),:(,etc.). The mood model is part of
 * the mood event which contains the other details such as settings and trigger.
 * @see MoodEvent
 */
public class Mood {
    private MoodState mood;
    private MoodColor color;
    private Image icon;

    /**
     * Setter method for mood, it will call a method stored inside the class through a switch
     * statement. Depending on what is picked, it will set the correct characteristics of that mood.
     * @param mood
     */
    public Mood(MoodState mood) {
        setMood(mood);
    }

    /**
     * Getter method for colour of the mood. Each mood state will have their own colours corresponding
     * to its emotion.
     * @return color of the mood
     */
    public MoodColor getColor() { return color; }

    /**
     * Getter method for the icon of the emotion. Emoticons such as :) and :( are some examples of
     * the icons used.
     * @return icon of the mood
     */
    public Image getIcon() { return icon; }

    /**
     * Getter method for the string literal version of the mood. The mood can range from happy to
     * shame.
     * @return mood string literal (happy, sad, etc.)
     */
    public MoodState getMood() { return mood; }

    /**
     * Setter method depending on the the mood that the participant has entered. Each mood will
     * have their corresponding details such as colour and icon attached to them.
     * @param mood
     */
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
