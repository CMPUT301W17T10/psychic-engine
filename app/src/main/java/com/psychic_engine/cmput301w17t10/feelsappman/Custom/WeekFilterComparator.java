package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;

import java.util.Comparator;

/**
 * Created by Hussain on 3/13/2017.
 */

/**
 * WeekFilterComparator just compares the date between two mood events, in attempt to filter them by
 * recent week.
 */
public class WeekFilterComparator implements Comparator<MoodEvent> {
    @Override
    public int compare(MoodEvent o1, MoodEvent o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
