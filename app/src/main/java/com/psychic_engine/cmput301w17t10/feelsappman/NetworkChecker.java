package com.psychic_engine.cmput301w17t10.feelsappman;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by adong on 2017-03-20.
 * Commented by adong on 3/29/2017
 */

public class NetworkChecker {

    /**
     * The method determines whether or not the participant is currently connected to a network of
     * some sort, whether it is a WI-FI or 3G/4G connection.
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
