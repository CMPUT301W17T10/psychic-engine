package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by adong on 2017-03-20.
 */
// TODO: Comments
public class ElasticSearchController extends ElasticController {

    public static class FindParticipantTask extends AsyncTask<String, Void, Participant> {

        @Override
        protected Participant doInBackground(String... params) {
            verifySettings();
            Participant foundParticipant = null;
            String query = "{\"from\" : 0, \"size\" : 1,\"query\" : {\"match\" : { \"login\" : \"" +params[0] + "\" }}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();


            try {
                Log.i("Attempt", "Search for " + params[0] + " Query: "+ query);
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Log.i("JSON", result.getJsonObject().toString());
                    foundParticipant = result.getSourceAsObject(Participant.class);
                }
                else {
                    Log.i("NULL", "Search returned null");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return foundParticipant;
        }
    }

}
