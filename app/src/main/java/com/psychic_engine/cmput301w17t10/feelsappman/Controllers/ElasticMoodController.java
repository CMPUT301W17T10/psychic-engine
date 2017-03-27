package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import static com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticController.verifySettings;

/**
 * Created by Airer on 3/21/2017.
 */

// TODO: Find by emoji | Find by reason | Find by location ???| need to find way to filter with params

public class ElasticMoodController extends ElasticController{

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
            /*
            if (params[1] == null) {
                query = "{\"query\": {\"filtered\": {\"query\": {\"match\":{\"moodOwner\": \n" +
                        params[0] + "\"}},\"filter\":{\"missing\":{\"field\":\"trigger\"}}}}}";
            }
            */
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

    public static class FindMoodByWeekTask extends AsyncTask<Void, Void, ArrayList<MoodEvent>> {

        @Override
        protected ArrayList<MoodEvent> doInBackground(Void... params) {
            verifySettings();
            ArrayList<MoodEvent> foundMoodEvents = new ArrayList<>();

            String query = "{\"query\" : {\"match\" : { \"\" : \"" + params[0] + "\"}}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("moodevent")
                    .build();

            try {
                Log.i("Attempt", "Search for "+ params[0] + " Query: "+ query);
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<MoodEvent> foundEvents = result.getSourceAsObjectList(MoodEvent.class);
                    foundMoodEvents.addAll(foundEvents);
                    return  foundMoodEvents;
                } else {
                    Log.i("None", "No mood events with this reason has been found");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error", "Communication error with server");
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
}
