package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Elastic search controller superclass that is used to createMoodEvent the variety of
 * controllers used throughout the program.
 * Comments by adong on 3/28/2017.
 * @author adong
 */
public abstract class ElasticController {

    static JestDroidClient client;

    /**
     * verifySettings ensures that the proper settings for each elastic request is satisfied. The
     * server address as well to ensure that there is a singleton client to handle these requests.
     */
    static void verifySettings() {
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
