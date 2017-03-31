package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * ElasticParticipantController handles all elastic requests in regards to participants. Tasks will
 * be used to update, add, and find participants to find other participants using the app.
 * Comments by adong on 3/28/2017.
 * @author adong
 */
public class ElasticParticipantController extends ElasticController {

    /**
     * AddParticipantTask deals with adding new participants into the server when the participant
     * signs up at the login page.
     */
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

    /**
     * FindParticipantTask is used to find a participant with a certain login name (case insensitive).
     * Usage for finding whether or not the name is already taken in the elastic server (satisfy
     * unique name requirement). A successful hit will mean that the name is already taken.
     */
    public static class FindParticipantTask extends AsyncTask<String, Void, Participant> {
        @Override
        protected Participant doInBackground(String... params) {
            verifySettings();
            Participant participantFound = null;

            // set a query to find 1 hit of a participant with the login name
            String query = "{\"size\" : 1,\"query\" : {\"match\" : { \"login\" : \"" +params[0] + "\" }}}";
            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();

            try {
                Log.i("Attempt", "Search for " + params[0] + " Query: "+ query);

                SearchResult result = client.execute(search);

                // successful hit
                if (result.isSucceeded()) {
                    participantFound = result.getSourceAsObject(Participant.class);
                    Log.i("F Participant", participantFound.getLogin());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return participantFound;
        }
    }

    /**
     * Elastic task method used to update a participant. Use this method when you need to update
     * participant data, such as following, followers, and mood events. Let the parameter be
     * the participant that you would like to update. Reindexing the participant is necessary
     * if there are significant changes that is not simply incrementing a value. Reindexing is done
     * by deleting the participant by ID and then adding the same participant again, preserving the
     * ID that was assigned to it previously.
     * @see Participant
     */
    public static class UpdateParticipantTask extends AsyncTask<Participant, Void, Void> {

        @Override
        protected Void doInBackground(Participant... updated) {
            Log.i("Update", "Attempting to update participants");
            verifySettings();
            for (Participant updatingParticipant : updated) {
                // need to delete the participant first
                String updateID = updatingParticipant.getId();
                try {
                    client.execute(new Delete.Builder(updateID).index("cmput301w17t10").type("participant").build());
                    Log.i("Success", "Deleted the participant");
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
            }
            return null;
            }
    }

    /**
     * Similar to the FindParticipantTask, this method will search for all participants that have
     * registered themselves into the server. This method is crucial for obtaining the most up to
     * date database of participants names when trying to login or sign up for a new account. This ensures
     * that unique names are always created (case insensitive).
     */
    public static class FindAllParticipantsTask extends AsyncTask<Void, Void, ArrayList<Participant>> {
        @Override
        protected ArrayList<Participant> doInBackground(Void... params) {
            verifySettings();
            ArrayList<Participant> participantList = new ArrayList<>();

            String query = "{\"size\" : 100,\"query\" : {\"match_all\" : {}}}";
            Log.i("Attempt", "Search for all participants: " + query);
            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t10")
                    .addType("participant")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Participant> resultList = result.getSourceAsObjectList(Participant.class);
                    Log.i("Size List", "Size of list: "+ String.valueOf(resultList.size()));
                    participantList.addAll(resultList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return participantList;
        }
    }
}
