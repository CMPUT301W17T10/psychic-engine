package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by adong on 3/20/17.
 */

public class ParticipantController {

    private ParticipantController(){}

    public static boolean checkUniqueParticipant(String participantName) {
        Log.i("Check", "Checking for uniqueness in " + participantName);
        Participant foundParticipant = null;
        ElasticParticipantController.FindParticipantTask spt = new ElasticParticipantController.FindParticipantTask();

        try {
            foundParticipant = spt.execute(participantName).get();
        } catch (Exception e) {
            Log.i("CheckParticipantName", "Failed connection with the elastic server");
        }
        return foundParticipant == null;
    }

    //TODO: Delete mood event from your mood event list

    public static void deleteMoodEvent(MoodEvent moodEvent) {
        ElasticMoodController.DeleteMoodEventTask deleteMoodEventTask = new ElasticMoodController
                .DeleteMoodEventTask();
        deleteMoodEventTask.execute(moodEvent);
    }

    //TODO: Update mood event from the mood event list
    /*
    public static void editMoodEvent(MoodEvent moodEvent) {
        ElasticMoodController.EditMoodEventTask editMoodEventTask = new ElasticMoodController
                .EditMoodEventTask();
        editMoodEventTask.execute(moodEvent);
    }
    */

    /**
     * updateSingletonList will attempt to pull the most up to date list of participants from the
     * elastic server in attempt to try and log in to the app as some participant that is already
     * registered. This makes it so that the app user will be able to login from any phone so long
     * an internet connection is there.
     */
    public static void updateSingletonList() {

        Log.i("Update", "Updating current list to elastic servers");
        ElasticParticipantController.FindAllParticipantsTask fpt = new ElasticParticipantController
                .FindAllParticipantsTask();
        ArrayList<Participant> singletonList = ParticipantSingleton.getInstance().getParticipantList();
        ArrayList<Participant> tempList;
        singletonList.clear();
        try {
            tempList = fpt.execute().get();
            for (Participant storedParticipant : tempList) {
                singletonList.add(storedParticipant);
            }
        } catch (Exception e) {
            Log.i("Error", "Unable to update singleton list with elastic");
        }

    }
}
