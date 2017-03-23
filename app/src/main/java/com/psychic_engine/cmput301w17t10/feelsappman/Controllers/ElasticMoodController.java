package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;

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

// TODO: Find by emoji | Find by reason | Find by location ???| Delete | Edit
public class ElasticMoodController extends ElasticController{

    public static class FindMoodByReasonTask extends AsyncTask<String, Void, ArrayList<MoodEvent>> {
        @Override
        protected ArrayList<MoodEvent> doInBackground(String ... params) {
            verifySettings();
            ArrayList<MoodEvent> foundMoodEvents = new ArrayList<>();

            String query = "{\"query\" : {\"match\" : { \"trigger\" : \"" + params[0] + "\"}}}";

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
                        Log.i("Success", "Mood event UUID: "+ moodEvent.getId());
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
}
