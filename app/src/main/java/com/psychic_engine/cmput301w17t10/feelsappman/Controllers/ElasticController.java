package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by adong on 2017-03-20.
 */

/**
 * Elastic search controller superclass that is used to create the variety of
 * controllers used throughout the program.
 */
public class ElasticController {
    protected static JestDroidClient client;
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
