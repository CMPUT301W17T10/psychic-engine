package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by adong on 2017-03-20.
 */

public class ElasticSearchController extends ElasticController {

    public static class SearchParticipantTask extends AsyncTask<String, Void, ArrayList<Participant>> {

        @Override
        protected ArrayList<Participant> doInBackground(String... participantsLogin) {
            verifySettings();
            ArrayList<Participant> participants = new ArrayList<Participant>();

            // TODO Build the query
            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "        \"term\" : { \"login\" : " + participantsLogin[0] + " }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<SearchResult.Hit<Participant, Void>> hits = result.getHits(Participant.class);
                    for (SearchResult.Hit results: hits) {
                        
                    }
                } else {
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return participants;
        }
    }
}
