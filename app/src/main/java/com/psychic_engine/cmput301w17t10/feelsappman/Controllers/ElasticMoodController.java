package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.RecentMapActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


// TODO: Find by emoji | Find by reason | Find by location ???| need to find way to filter with params

/**
 * ElasticMoodController handles any changes regarding moods, whether it would be updating, deleting,
 * or adding moods. Main method is to obtain the mood events in relation to the participant
 * currently using the app, then utilize the offline filtering to handle the required features
 * that a mood event should have such as mood, reason, and date.
 *
 * How to use:
 * ElasticMoodController.x task_name = new ElasticMoodController.x
 * (x is the method task you want to use ie. FilterMoodByReasonTask)
 * task_name.execute(parameters)
 * @author adong
 */
public class ElasticMoodController extends ElasticController{

    /**
     * AddMoodEventTask adds a mood event into the elastic server. Utilized when the participant
     * would like to createMoodEvent a mood event in the CreateMoodActivity.
     */
    public static class AddMoodEventTask extends AsyncTask<MoodEvent, Void, Void> {

        @Override
        protected Void doInBackground(MoodEvent... moodEvents) {
            verifySettings();

            // handling multiple mood events that need to be added
            Log.i("AddMoodEventTask", "Attempt to add mood event into es");
            for (MoodEvent moodEvent : moodEvents) {
                Index index = new Index.Builder(moodEvent).index("cmput301w17t10")
                        .type("moodevent").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        moodEvent.setId(result.getId());
                        Log.i("Success", "Mood event ID: "+ moodEvent.getId());
                        Log.i("ID", "Added mood event to Participant: " + ParticipantSingleton.getInstance().getSelfParticipant().getId());
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the participants");
                }
            }
            return null;
        }
    }

    /**
     * DeleteMoodEventTask is used when the participant would like to delete their mood events. The
     * task will only delete the mood event from the elastic server, and not from the participant's
     * mood event list locally.
     */
    public static class DeleteMoodEventTask extends AsyncTask<MoodEvent, Void, Void> {
        @Override
        protected Void doInBackground(MoodEvent... deleteMoods) {
            verifySettings();

            // for every mood needing to be deleted
            for (MoodEvent mood : deleteMoods) {
                String moodID = mood.getId();
                Log.i("Delete", "Currently deleting mood " + moodID);
                try {
                    client.execute(new Delete.Builder(moodID).index("cmput301w17t10").type("moodevent").build());
                } catch (Exception e) {
                    Log.i("Error", "Error deleting moods");
                }
            }
            return null;
        }
    }

    /**
     * Update task to editMoodEvent the mood events as you try to edit your mood. In offline mode, you
     * would be able to edit your moods, but as there is connection, it will editMoodEvent every mood event
     * that is in the participant's mood list, then editMoodEvent the entire participant with the updated
     * mood events.
     */
    public static class UpdateMoodTask extends AsyncTask<MoodEvent, Void, Void> {

        @Override
        protected Void doInBackground(MoodEvent... updateEvent) {
            Log.i("UpdateMoodTask", "Attempt to editMoodEvent mood event");
            // for every mood event that is added
            for (MoodEvent updatingMood : updateEvent) {
                String updateID = updatingMood.getId();
                try {
                    client.execute(new Delete.Builder(updateID).index("cmput301w17t10").type("moodevent").build());
                } catch (Exception e) {
                    Log.i("Error", "Unable to editMoodEvent mood into the server");
                }

                // then add the updated participant with the newer info (keep id)
                // createMoodEvent new index in the elastic with the same id before that was deleted
                Index index = new Index.Builder(updatingMood)
                        .index("cmput301w17t10")
                        .type("moodevent")
                        .id(updateID)
                        .build();
                try {
                    //execute the index command
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        updatingMood.setId(result.getId());
                        Log.i("Success", "Successful addition again");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Error updating in elastic");
                }
            }
            return null;
        }
    }
    public static class DeleteOfflineMoodEventTask extends AsyncTask<ArrayList<MoodEvent>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<MoodEvent>... deleteMoodList) {
            verifySettings();
            // for every mood needing to be deleted
            for (MoodEvent mood : deleteMoodList[0]) {
                String moodID = mood.getId();
                Log.i("Delete", "Currently deleting mood " + moodID);
                try {
                    client.execute(new Delete.Builder(moodID).index("cmput301w17t10").type("moodevent").build());
                } catch (Exception e) {
                    Log.i("Error", "Error deleting moods");
                }
            }
            return null;
        }
    }

    public static class AddOfflineMoodEventTask extends AsyncTask<ArrayList<MoodEvent>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<MoodEvent>... addMoodList) {
            verifySettings();

            // handling multiple mood events that need to be added
            for (MoodEvent moodEvent : addMoodList[0]) {
                Index index = new Index.Builder(moodEvent).index("cmput301w17t10")
                        .type("moodevent").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        moodEvent.setId(result.getId());
                        Log.i("Success", "Mood event ID: "+ moodEvent.getId());
                        Log.i("ID", "Added mood event to Participant: " + ParticipantSingleton.getInstance().getSelfParticipant().getId());
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the participants");
                }
            }
            return null;
        }
    }
}
