package com.psychic_engine.cmput301w17t10.feelsappman;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by adong on 2017-03-13.
 */

public class ElasticSearchParticipantController {
    private static JestDroidClient client;

    // Function to save new participants into the Elastic Server
    public static class AddNewParticipants extends AsyncTask<Participant, Void, Void>  {

        // Make it handle arbitrary number of arguments
        @Override
        protected Void doInBackground(Participant ... participants) {
            verifySettings();

            // Index for each participant
            for (Participant participant : participants) {
                Index index = new Index.Builder(participant).index("PARTICIPANT")
                        .type("Participant").build();

                // Attempt to create an index for the new participants to store into the server
                try {
                    DocumentResult result = client.execute(index);

                    // Upon successful execution of index creation, attempt to save id to participant
                    if (result.isSucceeded()) {
                        participant.setID(result.getId());
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

    // Function to retrieve participant using the app from Elastic Search
    public static class GetSelfParticipant extends AsyncTask<String, Void, ArrayList<Participant>> {

        // What arguments in the doInBackground
        // same argument type in onProgressUpdate
        @Override
        protected ArrayList<Participant> doInBackground (String ... searchParameters) {
            verifySettings();

            ArrayList<Participant> participantList = new ArrayList<Participant>();

            // Build query to send to server
            String query = "{\n" +
                    "   \"query\" : {\n" +
                    "       \"term\" : { \"login\" : "+ searchParameters[0] +" }\n" +
                    "   }\n" +
                    "}";
            Search search = new Search.Builder(query)
                    .addIndex("PARTICIPANT")
                    .addType("Participant")
                    .build();

            // Execute search of the query in attempt to get results
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Participant selfParticipant = result.getSourceAsObject(Participant.class);
                }

            } catch (Exception e) {
                Log.i("Error", "Elasticsearch attempt failed!");
            }
            return participantList;
        }
    }

    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder
                    ("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
