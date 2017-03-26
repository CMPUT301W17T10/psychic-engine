package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;

/**
 * Created by Airer on 3/25/2017.
 */

// mappings and resets
public class ElasticMasterController extends ElasticController {

    private static final String INDEX = "cmput301w17t10";
    private static final String P_TYPE = "participant";
    private static final String M_TYPE = "mood";

    public static class ResetElasticServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            verifySettings();

            // delete cmput301/w17/t10 index to delete entire section
            //DeleteIndex deleteIndexParticipant = new DeleteIndex.Builder(INDEX).type(P_TYPE).build();
            //DeleteIndex deleteIndexMood = new DeleteIndex.Builder(INDEX).type(M_TYPE).build();
            DeleteIndex deleteIndex = new DeleteIndex.Builder(INDEX).build();
            try {
                JestResult resultParticipant = client.execute(deleteIndex);
                if (resultParticipant.isSucceeded()) {
                    Log.i("Success", "Deleted participant type");
                }
            } catch(Exception e) {
                Log.i("Failed", "Failed to delete index");
            }

            /*
            // create a fresh index and types with custom mapping for nested moods
            try {
                JestResult result = client.execute(new CreateIndex.Builder(INDEX).build());
                if (result.isSucceeded()) {
                    Log.i("Success", "Succesful reset");
                    //createParticipantMapping();
                    //createMoodMapping();
                }
            } catch (Exception e) {
                Log.i("Failed", "Failed to create new index");
            }
            */
            return null;
        }
    }

    /*
    private static void createParticipantMapping() {
        PutMapping putMapping = new PutMapping.Builder(INDEX, P_TYPE,
                "{\n" +
                "  \"participant\": {\n" +
                "    \"properties\": {\n" +
                "      \"login\": {\"type\": \"string\", \"store\" : \"yes\"},\n" +
                "      \"uniqueID\": {\"type\": \"string\", \"store\" : \"yes\"},\n" +
                "      \"followers\" : { \"type\": \"string\", \"store\" : \"yes\" , \"index\":\"not_analyzed\" },\n" +
                "      \"following\" : { \"type\": \"string\", \"store\" : \"yes\" , \"index\":\"not_analyzed\"}, \n" +
                "      \"pendingRequests\" : { \"type\": \"string\", \"store\" : \"yes\" , \"index\":\"not_analyzed\" }\n" +
                "    }\n" +
                "  }\n" +
                "}")
                .build();
        try {
            JestResult result = client.execute(putMapping);
            if (result.isSucceeded()) {
                Log.i("MappingGood", "Mapping success for participant");
            }

        }catch( Exception e ) {
            e.printStackTrace();
            Log.i("Fail", "Failed mapping for the participant");
        }
    }

    private static void createMoodMapping() {
        PutMapping putMapping = new PutMapping.Builder(INDEX, M_TYPE,
                "{\n" +
                        "  \"moodevent\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"date\": {\"type\": \"date\", \"store\" : \"yes\"},\n" +
                        "      \"moodOwner\": {\"type\": \"string\", \"store\" : \"yes\"},\n" +
                        "      \"mood\" : { \"type\": \"nested\", \"properties\" : { \"mood\" : {\"type\":\"string\"},\"color\" :{\"type\":\"string\" }}}\n" +
                        "      \"trigger\" : { \"type\": \"string\", \"store\" : \"yes\"}, \n" +
                        "      \"location\" : { \"type\": \"string\", \"store\" : \"yes\"}, \n" +
                        "      \"photo\" : { \"type\": \"string\", \"store\" : \"yes\"}, \n" +
                        "      \"followers\" : { \"type\": \"string\", \"store\" : \"yes\" , \"index\":\"not_analyzed\" }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"
                )

                .build();
        try {
            JestResult result = client.execute(putMapping);
            if (result.isSucceeded()) {
                Log.i("MappingGood", "Mapping success for mood event");
            }

        }catch( Exception e ) {
            e.printStackTrace();
            Log.i("Fail", "Failed mapping for the moodevent");
        }
    }
    */
}
