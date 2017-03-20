package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jyuen1 on 3/17/17.
 */

public class DeleteMoodController {

    /**
     * Removes the specified moodEvent from the self participant's mood list.
     * @param moodEvent
     */
    public static void remove(MoodEvent moodEvent) {
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        ArrayList<MoodEvent> moodEventList = participant.getMoodList();

        // remove the mood event from the participants mood event list
        moodEventList.remove(moodEvent);

        MoodEvent mostRecent;
        // update the most recent mood
        if (moodEventList.size() > 0) {
            mostRecent = moodEventList.get(0);
            Date mostRecentDate = mostRecent.getDate();
            for (MoodEvent m : moodEventList) {
                if (m.getDate().after(mostRecentDate)) {
                    mostRecent = m;
                    mostRecentDate = m.getDate();
                }
            }

        } else {
            mostRecent = null;
        }
        participant.setMostRecentMoodEvent(mostRecent);
    }
}
