package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 * Created by jordi on 2017-03-09.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.psychic_engine.cmput301w17t10.feelsappman.R;


public class SummaryTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary, container, false);

        return rootView;


    }
}
