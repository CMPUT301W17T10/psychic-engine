package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 *  Created by Hussain Khan     Modified by jyuen1
 *  HistoryTabFragment is the History tab that can be
 *  seen from SelfNewsFeedActivity. It shows a list view
 *  of all the Mood Events created. From this page, you
 *  can apply one or more filters to search for a particular
 *  Mood Event
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.psychic_engine.cmput301w17t10.feelsappman.Comparators.CustomComparator;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryTabFragment extends Fragment {

    private ListView moodEventsListView;
    private CheckBox filterDate;
    private CheckBox filterWeek;
    private EditText filterTrigger;
    private Button applyFilters;
    private Boolean dateFilterSelected;
    private Boolean weekFilterSelected;
    private Boolean moodFilterSelected;
    private Boolean triggerFilterSelected;
    private Spinner moodSpinner;
    private boolean satisfiesMood;
    private boolean satisfiesDate;
    private boolean satisfiesTrigger;
    private ArrayList<MoodEvent> filteredMoodList;
    private ArrayList<MoodEvent> unfilteredMoodList;
    private ArrayAdapter<MoodEvent> adapter;
    private final long oneWeek = 604800000L;    // one weeks time

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);

        // Temporary list for filtered results is initially a copy of the original list
        unfilteredMoodList = ParticipantSingleton.getInstance().getSelfParticipant().getMoodList();
        filteredMoodList = new ArrayList<MoodEvent>(unfilteredMoodList);


        //initialize clickables
        moodEventsListView = (ListView) rootView.findViewById(R.id.moodEventsList);
        filterDate = (CheckBox)rootView.findViewById(R.id.recentfilter);
        filterWeek = (CheckBox)rootView.findViewById(R.id.weekfilter);
        filterTrigger = (EditText) rootView.findViewById(R.id.filterreason);
        applyFilters = (Button) rootView.findViewById(R.id.applyfilters);
        moodSpinner = (Spinner) rootView.findViewById(R.id.moodsspinner);

        // Spinner drop down elements
        List<String> moodCategories = new ArrayList<String>();
        moodCategories.add("None");     // default option
        MoodState[] moodStates = MoodState.values();
        for (MoodState moodState : moodStates) {
            moodCategories.add(moodState.toString());
        }

        // Set adapter for spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, moodCategories);
        moodSpinner.setAdapter(adapterSpinner);



        //apply the filters now
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter();
                adapter.notifyDataSetChanged();
            }
        });


        // enable viewing mood event on tap of list item


        // enable edit and delete options on long click of a list item
        moodEventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return false;
            }
        });


        return rootView;
    }



    private void filter() {
        filteredMoodList.clear();
        
        // Check which filters have been selected
        checkFilterSelected();

        for (MoodEvent moodEvent : unfilteredMoodList) {

            satisfiesMood = true;
            satisfiesDate = true;
            satisfiesTrigger = true;

            // check if mood event satisfies mood filter
            if (moodFilterSelected && !(moodEvent.getMood().getMood().toString().equals(
                    moodSpinner.getSelectedItem().toString())))
                satisfiesMood = false;

            // check if mood event satisfies trigger filter
            if (triggerFilterSelected && !moodEvent.getTrigger().toLowerCase().
                    contains(filterTrigger.getText().toString().toLowerCase()))
                satisfiesTrigger = false;

            // check if mood event satisfies date filter
            if (weekFilterSelected && moodEvent.getDate().before(
                    new Date(new Date().getTime() - oneWeek)))
                satisfiesDate = false;

            // add mood event to the list to be displayed if it satisfies all conditions
            if (satisfiesMood && satisfiesDate && satisfiesTrigger)
                filteredMoodList.add(moodEvent);

        }

        // sort mood events in reverse chronological order
        if (dateFilterSelected)
            Collections.sort(filteredMoodList, new CustomComparator());

    }



    private void checkFilterSelected() {

        // Check if the date filter is selected
        dateFilterSelected = (filterDate.isChecked() ? true : false);

        // Check if the week filter is selected
        weekFilterSelected = (filterWeek.isChecked() ? true : false);

        // Check if trigger filter is selected
        triggerFilterSelected = (!filterTrigger.getText().toString().
                equals("") ? true : false);

        // Check if mood is selected in mood filter
        moodFilterSelected = (!moodSpinner.getSelectedItem().toString().
                equals("None") ? true : false);

    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new ArrayAdapter<MoodEvent>(getActivity(), R.layout.item_history, filteredMoodList);
        moodEventsListView.setAdapter(adapter);
    }

}
