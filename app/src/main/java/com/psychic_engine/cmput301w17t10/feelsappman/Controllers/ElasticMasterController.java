package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;

/**
 * Created by adong on 3/25/2017.
 * Comments by adong on 3/28/2017.
 */

/**
 * Elastic master controller that has the ability to reset the elastic server. Adding another
 * participant or mood event will automatically create another index and type for whatever you
 * want to add.
 * @author adong
 * @see ElasticController
 * @see ElasticMoodController
 * @see ElasticParticipantController
 */
public class ElasticMasterController extends ElasticController {

    private static final String INDEX = "cmput301w17t10";

    /**
     * ResetElasticServer is a command that will reset the elastic server by deleting our index
     * which contains all of our participants and types. More designed to be for an easy way
     * to reset the database and create new data through the use of the generate data button in the
     * login activity. Upon addition of new data into the elastic server, a new index and their
     * types will also be recreated
     */
    public static class ResetElasticServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            verifySettings();

            // delete cmput301/w17/t10 index to delete entire section
            DeleteIndex deleteIndex = new DeleteIndex.Builder(INDEX).build();
            try {
                JestResult resultParticipant = client.execute(deleteIndex);
                if (resultParticipant.isSucceeded()) {
                    Log.i("Success", "Deleted cmput301w17t10 index");
                }
            } catch(Exception e) {
                Log.i("Failed", "Failed to delete index");
            }
            return null;
        }
    }

}
