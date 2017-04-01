package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by adong on 3/20/17.
 * Comments by adong on 3/28/2017.
 */

public class ParticipantController {

    private ParticipantController(){}

    /**
     * checkUniqueParticipant is a method that will determine whether or not a certain participant
     * name is taken upon sign up or log in. When you sign up, we need this method to be false, to
     * ensure that there are no other names in the server and that the unique quality is satisfied.
     * When you log in, the method should be true, to ensure that there is a participant stored
     * in the server and its information can be retrieved for use in the app.
     * @param participantName
     * @return true if name is found
     * @return false if name is not found
     */
    public static boolean checkUniqueParticipant(String participantName) {
        Participant foundParticipant = null;
        ElasticParticipantController.FindParticipantTask spt = new ElasticParticipantController.FindParticipantTask();

        try {
            foundParticipant = spt.execute(participantName).get();
        } catch (Exception e) {
            Log.i("CheckParticipantName", "Failed connection with the elastic server");
        }
        return foundParticipant == null;
    }

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
            Log.i("Error", "Unable to editMoodEvent singleton list with elastic");
        }
    }

    public static void addMoodEvent(MoodEvent moodEvent) {
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        participant.getMoodList().add(moodEvent);
    }
}
