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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Fragment class to display the tab in the profile's UI. This tab will display a list of past
 * mood events that their followers made.
 */
public class HistoryTabFragment extends Fragment {
    private ListView moodEventsListView;
    public ArrayList<MoodEvent> moodEventsHistory;
    public ArrayAdapter<MoodEvent> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moodEventsHistory = ParticipantSingleton.getInstance().getSelfParticipant().getMoodList();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);

        moodEventsListView = (ListView) rootView.findViewById(R.id.moodEventsList);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.moodsspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.moodsspinnerarray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        adapter = new ArrayAdapter<MoodEvent>(getActivity(), R.layout.item_history, moodEventsHistory);
        moodEventsListView.setAdapter(adapter);
    }
}
