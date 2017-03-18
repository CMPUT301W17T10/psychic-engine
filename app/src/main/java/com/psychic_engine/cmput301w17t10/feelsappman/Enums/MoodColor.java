package com.psychic_engine.cmput301w17t10.feelsappman.Enums;

/**
 * Created by jyuen1 on 2/27/17.
 */

public enum MoodColor {
    // Colors obtained from https://material.io/guidelines/style/color.html#color-color-palette
    BLUE,
    GREEN,
    PURPLE,
    ORANGE,
    RED,
    PINK,
    LIME,
    YELLOW;

    @Override
    public String toString() {
        switch(this) {
            case BLUE: return "Blue";
            case GREEN: return "Green";
            case PURPLE: return "Purple";
            case ORANGE: return "Orange";
            case RED: return "Red";
            case PINK: return "Pink";
            case LIME: return "Light Green";
            case YELLOW: return "Yellow";
            default: throw new IllegalArgumentException();
        }
    }

    public String getBGColor() {
        switch(this) {
            case BLUE: return "#1A237E";
            case GREEN: return "#1B5E20";
            case PURPLE: return "#4A148C";
            case ORANGE: return "#BF360C";
            case RED: return "#B71C1C";
            case PINK: return "#880E4F";
            case LIME: return "#827717";
            case YELLOW: return "#FFAB00";
            default: throw new IllegalArgumentException();
        }
    }


}

