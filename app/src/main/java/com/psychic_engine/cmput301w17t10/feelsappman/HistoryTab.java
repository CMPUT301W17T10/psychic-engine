package com.psychic_engine.cmput301w17t10.feelsappman;

/**
 * Created by jordi on 2017-03-09.
 * Comments by Alex on 2017-03-12.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment class to display the tab in the profile's UI. This tab will display a list of past
 * mood events that their followers made.
 */
public class HistoryTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);

        return rootView;


    }
}
