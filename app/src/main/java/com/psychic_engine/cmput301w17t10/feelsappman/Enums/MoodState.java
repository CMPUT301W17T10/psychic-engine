package com.psychic_engine.cmput301w17t10.feelsappman.Enums;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
/**
 * Created by jyuen1 on 2/27/17.
 */

/**
 * MoodState contains the physical emotion that is chosen by the participant. The mood state is one
 * of two parts to the class of Mood. Each mood state has their own color that represents their mood.
 * @see MoodColor
 * @see Mood
 */
public enum MoodState {
    SAD,
    HAPPY,
    SHAME,
    FEAR,
    ANGER,
    SURPRISED,
    DISGUST,
    CONFUSED;

    /**
     * Override method of toString() to return the string of some mood state that was obtained by
     * the spinner when creating a mood event or editing an already existing mood event.
     * @return String of the mood state
     */
    @Override
    public String toString() {
        switch(this) {
            case SAD: return "Sad";
            case HAPPY: return "Happy";
            case SHAME: return "Shame";
            case FEAR: return "Fear";
            case ANGER: return "Anger";
            case SURPRISED: return "Surprised";
            case DISGUST: return "Disgust";
            case CONFUSED: return "Confused";
            default: throw new IllegalArgumentException();
        }
    }

}

