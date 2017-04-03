package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.content.Context;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jyuen1 on 3/17/17.
 * Comments by adong on 3/28/2017
 */

/**
 * DeleteMoodController will delete mood events from the participant's mood event list. This will
 * include
 */
public class DeleteMoodController extends MoodController {

    /**
     * Removes the specified moodEvent from the self participant's mood list. Update the new
     * participent details in the elastic server
     * @param moodEvent
     */
    public static void deleteMoodEvent(MoodEvent moodEvent, Context context) {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        // delete the mood event from the server
        if (isConnected(context)) {
            ElasticMoodController.DeleteMoodEventTask dmt = new ElasticMoodController.DeleteMoodEventTask();
            dmt.execute(moodEvent);
        } else {
            if (instance.getOfflineCreatedList().contains(moodEvent)) {
                instance.getOfflineCreatedList().remove(moodEvent);
            }
            else if (instance.getOfflineEditList().contains(moodEvent)) {
                instance.getOfflineEditList().remove(moodEvent);
            }
            else {
                instance.addDeleteOfflineMood(moodEvent);
            }
        }
        // get the participant's mood list to delete from locally
        Participant participant = instance.getSelfParticipant();
        ArrayList<MoodEvent> moodEventList = participant.getMoodList();

        // deleteMoodEvent the mood event from the participants mood event list
        moodEventList.remove(moodEvent);

        MoodEvent mostRecent;
        // editMoodEvent the most recent mood
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

        // editMoodEvent the participant to stay up to date with in the elastic server
        if (isConnected(context)) {
            ElasticParticipantController.UpdateParticipantTask upt = new ElasticParticipantController.UpdateParticipantTask();
            upt.execute(participant);
        }
    }
}
