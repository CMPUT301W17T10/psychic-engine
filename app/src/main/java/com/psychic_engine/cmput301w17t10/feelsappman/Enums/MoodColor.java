package com.psychic_engine.cmput301w17t10.feelsappman.Enums;

/**
 * Created by jyuen1 on 2/27/17.
 */

public enum MoodColor {
    BLUE,
    GREEN,
    PURPLE,
    ORANGE,
    RED,
    PINK,
    LIGHTGREEN,
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
            case LIGHTGREEN: return "Light Green";
            case YELLOW: return "Yellow";
            default: throw new IllegalArgumentException();
        }
    }


}

