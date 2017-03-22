package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Airer on 3/21/2017.
 */

// TODO: Find by emoji | Find by reason | Find by location ??? | Add | Delete | Edit
public class ElasticMoodController extends ElasticController{

    public static class FindMoodReasonTask extends AsyncTask<String, Void, ArrayList<MoodEvent>> {
        @Override
        protected ArrayList<MoodEvent> doInBackground(String ... params) {
            verifySettings();
            ArrayList<MoodEvent> foundMoodEvents = null;

            String query = "{\"query\" : {\"match\" : { \"reason\" : \"" + params[0] + "\"}" +
                    " \"login\" : \""+ params[1] + "\"}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();

            try {
                Log.i("Attempt", "Search for "+ params[0] + " Query: "+ query);
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<MoodEvent> foundEvents = result.getSourceAsObjectList(MoodEvent.class);
                    foundMoodEvents.addAll(foundEvents);
                } else {
                    Log.i("None", "No mood events with this reason has been found");
                    return null;
                }
            } catch (Exception e) {
                Log.i("Error", "Communication error with server");
            }

            return foundMoodEvents;
        }
    }

    public static class AddMoodEventTask extends AsyncTask<MoodEvent, Void, Void> {

        //TODO: Assign the participants UID to their own mood events ???
        @Override
        protected Void doInBackground(MoodEvent... moodEvents) {
            verifySettings();

            for (MoodEvent moodEvent : moodEvents) {
                Index index = new Index.Builder(moodEvent).index("cmput301w17t10")
                        .type("moodevent").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.i("Success", "Mood event added into the server!");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the participants");
                }
            }
            return null;
        }
    }
}
