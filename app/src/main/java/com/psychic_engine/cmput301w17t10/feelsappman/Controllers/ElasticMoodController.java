package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;
import java.util.List;

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
     * FilterMoodByReasonTask takes in two parameters, the first one being the login name of participant
     * currently using the app. The second parameter is the reason (trigger) for the mood event.
     * The task filters the mood events that the participant has through the trigger field.
     */
    public static class FilterMoodByReasonTask extends AsyncTask<String, Void, ArrayList<MoodEvent>> {
        @Override
        protected ArrayList<MoodEvent> doInBackground(String ... params) {
            verifySettings();
            String query;
            ArrayList<MoodEvent> foundMoodEvents = new ArrayList<>();

            //if params[1] is null (trigger) : use the missing field
            //if params[1] is not null (trigger) : use the second query with the reason text
            // params[0] is the current participant filtering their moods
            String self = params[0];
            String reason = params[1];
            Log.i("Self","Logged in as "+ self);
            Log.i("Trigger", "Search for "+ reason);

            // would not need to filter by reason if the trigger had no reason in the edit text
            // would change the method if there was no filter by reason intended.
            query = "{\"query\": {\"match\":{\"moodOwner\":\"" +
                    "" + self + "\"}},\"filter\":{\"term\":{\"trigger\":\""+reason+"\"}}}";

            Log.i ("QUERY", query);

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("moodevent")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<MoodEvent> foundEvents = result.getSourceAsObjectList(MoodEvent.class);
                    Log.i("Size List", "Size of list: "+ String.valueOf(foundEvents.size()));
                    foundMoodEvents.addAll(foundEvents);
                } else {
                    Log.i("None", "No mood events with this reason has been found");
                }
            } catch (Exception e) {
                Log.i("Error", "Communication error with server");
            }
            return foundMoodEvents;
        }

    }

    /**
     * AddMoodEventTask adds a mood event into the elastic server. Utilized when the participant
     * would like to create a mood event in the CreateMoodActivity.
     */
    public static class AddMoodEventTask extends AsyncTask<MoodEvent, Void, Void> {

        @Override
        protected Void doInBackground(MoodEvent... moodEvents) {
            verifySettings();

            // handling multiple mood events that need to be added
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
                try {
                    // get the id given when they were added to the elastic server
                    String moodID = mood.getId();
                    client.execute(new Delete.Builder(moodID).index("cmput301w17t10").type("moodevent").build());
                } catch (Exception e) {
                    Log.i("Error", "Error deleting moods");
                }
            }
            return null;
        }
    }

    /**
     * Update task to update the mood events as you try to edit your mood. In offline mode, you
     * would be able to edit your moods, but as there is connection, it will update every mood event
     * that is in the participant's mood list, then update the entire participant with the updated
     * mood events.
     */
    public static class UpdateMoodTask extends AsyncTask<MoodEvent, Void, Void> {

        @Override
        protected Void doInBackground(MoodEvent... updateEvent) {

            // for every mood event that is added
            for (MoodEvent updatingMood : updateEvent) {
                String updateID = updatingMood.getId();
                try {
                    client.execute(new Delete.Builder(updateID).index("cmput301w17t10").type("moodevent").build());
                } catch (Exception e) {
                    Log.i("Error", "Unable to update mood into the server");
                }

                // then add the updated participant with the newer info (keep id)
                // create new index in the elastic with the same id before that was deleted
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

    /**
     * FindMoodLocationsTask find all mood events of any participant that has entered a mood event
     * that has location enabled when they made their mood
     */
    public static class FindMoodLocationsTask extends AsyncTask<Void, Void, ArrayList<MoodEvent>> {

        @Override
        protected ArrayList<MoodEvent> doInBackground(Void... params) {
            verifySettings();
            ArrayList<MoodEvent> foundMoods = new ArrayList<>();
            String query = "{\"size\": 10000,\"query\" : {\"filtered\" : { \"filter\" : " +
                    "{ \"bool\" : {\"must_not\""+
                    ": [ {\"missing\": {\"field\" : \"location\"}}]}}}}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("moodevent")
                    .build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<MoodEvent> resultList = result.getSourceAsObjectList(MoodEvent.class);
                    foundMoods.addAll(resultList);
                    Log.i("Success", "Successfully pulled mood events with locations");
                }
            } catch (Exception e) {
                Log.i("Fail", "Failed to obtain mood events with locations");
            }
            return foundMoods;
        }
    }

    /**
     *  Find all mood events in the elastic server to then be sorted through locally with each
     *  participant's most recent mood event and display the event if it does have a location. The
     *  mood events are sorted in descending order (most recent)
     */
    public static class FindMoodEventsTask extends AsyncTask<Void, Void, ArrayList<MoodEvent>> {

        @Override
        protected ArrayList<MoodEvent> doInBackground(Void... params) {
            verifySettings();
            ArrayList<MoodEvent> foundMoods = new ArrayList<>();
            String query = "{\"size\": 10000 , \"query\":{\"sort\" : { \"date\" : { \"order\": \"desc\"}}}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("moodevent")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<MoodEvent> resultList = result.getSourceAsObjectList(MoodEvent.class);
                    foundMoods.addAll(resultList);
                    Log.i("Success", "Successfully pulled mood events with locations");
                }
            } catch (Exception e) {
                Log.i("Fail", "Failed to obtain mood events with locations");
            }
            return foundMoods;
        }
    }
}
