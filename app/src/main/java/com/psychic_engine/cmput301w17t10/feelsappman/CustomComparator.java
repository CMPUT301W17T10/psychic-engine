package com.psychic_engine.cmput301w17t10.feelsappman;

import java.util.Comparator;

/**
 * Created by Hussain on 3/13/2017.
 */

public class CustomComparator implements Comparator<MoodEvent> {
    @Override
    public int compare(MoodEvent o1, MoodEvent o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
