package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Elastic search controller superclass that is used to create the variety of
 * controllers used throughout the program.
 * Comments by adong on 3/28/2017.
 * @author adong
 */
public class ElasticController {

    protected static JestDroidClient client;

    /**
     * verifySettings ensures that the proper settings for each elastic request is satisfied. The
     * server address as well to ensure that there is a singleton client to handle these requests.
     */
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
/*
Search bar to find followers
Find all participants that have some thing in the edittext as their name  on click
term for case sensitive (will not return hits if you search for Pierre when you have pierre)
match for non case sensitive (will return hits if you search for Pierre when you have pierre)
{
    "query": {
        "match" : { "login" : _____ }
    }
}
 */
