package com.psychic_engine.cmput301w17t10.feelsappman.Enums;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
/**
 * Created by jyuen1 on 2/27/17.
 */

/**
 * MoodColor determines a mood states respective colour. Each mood state will have their own colour
 * to represent their mood. This colour will be displayed as a background when a participant
 * views the mood event. Mood color is part of the class Mood which also contains its mood state
 * @see Mood
 * @see MoodState
 */
public enum MoodColor {
    // Colors obtained from https://material.io/guidelines/style/color.html#color-color-palette
    BLUE,
    GREEN,
    PURPLE,
    ORANGE,
    RED,
    PINK,
    BROWN,
    YELLOW;

    /**
     * An override method to get the string form of some color we would like, that was obtained
     * from the mood classes.
     * @see Mood
     * @return String version of the color
     */
    @Override
    public String toString() {
        switch(this) {
            case BLUE: return "Blue";
            case GREEN: return "Green";
            case PURPLE: return "Purple";
            case ORANGE: return "Orange";
            case RED: return "Red";
            case PINK: return "Pink";
            case BROWN: return "Light Green";
            case YELLOW: return "Yellow";
            default: throw new IllegalArgumentException();
        }
    }

    /**
     * getBGColor will return a hex code that will be read to be a certain colour. This will determine
     * the background colour of some mood event.
     * @return
     */
    public String getBGColor() {
        switch(this) {
            case BLUE: return "#1A237E";
            case GREEN: return "#1B5E20";
            case PURPLE: return "#4A148C";
            case ORANGE: return "#BF360C";
            case RED: return "#B71C1C";
            case PINK: return "#880E4F";
            case BROWN: return "#3E2723";
            case YELLOW: return "#FFAB00";
            default: throw new IllegalArgumentException();
        }
    }

    public String getChartColor() {
        switch(this) {
            case BLUE: return "#2196F3";
            case GREEN: return "#76FF03";
            case PURPLE: return "#B388FF";
            case ORANGE: return "#FF6D00";
            case RED: return "#F44336";
            case PINK: return "#F48FB1";
            case BROWN: return "#795548";
            case YELLOW: return "#FFFF00";
            default: throw new IllegalArgumentException();
        }
    }


}
