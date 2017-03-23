package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Created by adong on 2017-03-13.
 */

// TODO: Comments
public class ElasticParticipantController extends ElasticController {
    // Function to save new participants
    public static class AddParticipantTask extends AsyncTask<Participant, Void, Void>  {

        // Make it handle arbitrary number of arguments
        @Override
        protected Void doInBackground(Participant... participants) {
            verifySettings();

            // Index for each participant
            for (Participant participant : participants) {
                Index index = new Index.Builder(participant).index("cmput301w17t10")
                        .type("participant").build();

                // Attempt to create an index for the new participants to store into the server
                try {
                    DocumentResult result = client.execute(index);
                    // Upon successful execution of index creation, attempt to save uniqueID to participant
                    if (result.isSucceeded()) {
                        Log.i("Success", "Participant UUID: "+participant.getID());
                    }
                    else {
                        Log.i("Error", "Elasticsearch was not able to add the new participant");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the participants");
                }
            }
            return null;
        }
    }

    public static class FindParticipantTask extends AsyncTask<String, Void, Participant> {
        @Override
        protected Participant doInBackground(String... params) {
            verifySettings();
            Participant foundParticipant = null;
            String query = "{\"size\" : 1,\"query\" : {\"term\" : { \"login\" : \"" +params[0] + "\" }}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();

            try {
                Log.i("Attempt", "Search for " + params[0] + " Query: "+ query);
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    foundParticipant = result.getSourceAsObject(Participant.class);
                    Log.i("Found", "Found the participant name: "+ foundParticipant.getLogin());
                }
                else {
                    return null;
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return foundParticipant;
        }
    }

    public static class AddFollowerTask extends AsyncTask<Participant, Void, Void> {
        @Override
        protected Void doInBackground(Participant... follower) {
            verifySettings();
            Participant followingParticipant = follower[0];
            String followingID = followingParticipant.getID();
            String script = "{\n" +
                    "    \"script\" : \"ctx._source.following += new_following\",\n" +
                    "    \"params\" : {\n" +
                    "        \"new_following\" : \""+follower[1].getID()+"\"\n" +
                    "    }\n" +
                    "}";

            try {
                client.execute(new Update.Builder(script).index("cmput301w17t10")
                        .type("participant").id(followingID).build());
            } catch (Exception e) {
                Log.i("Error", "Unable to add follower into the server");
            }

            return null;
        }
    }
}
