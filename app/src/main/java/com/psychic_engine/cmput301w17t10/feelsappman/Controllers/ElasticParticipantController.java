package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by adong on 2017-03-13.
 */

// TODO: Comments
    //able to add participant on signup
    //able to follow a participant's recent mood event on follow request
    //able to search for a participant's name and their recent mood event
    //update participant at anytime if information is changed by edit


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
                        participant.setId(result.getId());
                        Log.i("Success", "Participant UUID: "+participant.getId());
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
            Participant singleParticipant = null;
            String query = "{\"size\" : 1,\"query\" : {\"term\" : { \"login\" : \"" +params[0] + "\" }}}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();

            try {
                Log.i("Attempt", "Search for " + params[0] + " Query: "+ query);
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    singleParticipant = result.getSourceAsObject(Participant.class);
                    Log.i("Found", "Found the participant name: "+ singleParticipant.getLogin());
                }
                else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return singleParticipant;
        }
    }

    /**
     * Elastic task method used to update a participant. Use this method when you need to update
     * participant data, such as following, followers, and mood events.
     * @see Participant
     */
    public static class UpdateParticipantTask extends AsyncTask<Participant, Void, Void> {
        /*
        add follower to participant's following (prior)
        delete participant from the elastic server
        add participant again with the updated info
        follower[0] is the following participant (getting a follower)
        follower[1] is the follower participant (giving a follow)
        would utilize add/delete tasks, but unable to implement
         */
        @Override
        protected Void doInBackground(Participant... updated) {
            verifySettings();
            Participant updatingParticipant = updated[0];
            // need to delete the participant first and add the participant again
            String updateID = updatingParticipant.getId();
            try {
                client.execute(new Delete.Builder(updateID).index("cmput301w17t10").type("participant").build());
                Log.i("Sucess", "Deleted the participant");
            } catch (Exception e) {
                Log.i("Error", "Unable to add follower into the server");
            }

            // then add the updated participant with the newer info (keep id)
            // create new index in the elastic with the same id before that was deleted

            Index index = new Index.Builder(updatingParticipant)
                    .index("cmput301w17t10")
                    .type("participant")
                    .id(updateID)
                    .build();
            try {
                //execute the index command
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    updatingParticipant.setId(result.getId());
                    Log.i("Success", "Successful addition again");
                }
            } catch (Exception e) {
                Log.i("Error", "Error updating in elastic");
            }

            return null;
        }
    }
}
