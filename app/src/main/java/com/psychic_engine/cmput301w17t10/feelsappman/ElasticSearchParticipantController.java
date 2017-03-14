package com.psychic_engine.cmput301w17t10.feelsappman;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

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
                Index index = new Index.Builder(participant).index("PARTICIPANT").type("Participant")
                        .build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        participant.setId(result.getId());
                    }

                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the participants");
                }
            }
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
