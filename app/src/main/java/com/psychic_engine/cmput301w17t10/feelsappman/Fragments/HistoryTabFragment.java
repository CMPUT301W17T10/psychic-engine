package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 *  Created by Hussain Khan
 *  HistoryTabFragment is the History tab that can be
 *  seen from SelfNewsFeedActivity. It shows a list view
 *  of all the Mood Events created. From this page, you
 *  can apply one or more filters to search for a particular
 *  Mood Event
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.psychic_engine.cmput301w17t10.feelsappman.Comparators.CustomComparator;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HistoryTabFragment extends Fragment {
    private ListView moodEventsListView;
    private CheckBox filterDate;
    private CheckBox filterWeek;
    private EditText filterReason;
    private Button applyFilters;
    private Boolean dateFilterSelected;
    private Boolean weekFilterSelected;
    private Boolean moodFilterSelected;
    private Boolean reasonFilterSelected;
    private String spinnerText;
    public ArrayList<MoodEvent> moodEventsHistory;
    public ArrayAdapter<MoodEvent> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);

        //load events list
        moodEventsHistory = new ArrayList<MoodEvent>();
        reloadList();

        //initialize clickables
        moodEventsListView = (ListView) rootView.findViewById(R.id.moodEventsList);
        filterDate = (CheckBox)rootView.findViewById(R.id.recentfilter);
        filterWeek = (CheckBox)rootView.findViewById(R.id.weekfilter);
        filterReason = (EditText) rootView.findViewById(R.id.filterreason);
        applyFilters = (Button) rootView.findViewById(R.id.applyfilters);

        //set booleans to false
        dateFilterSelected = false;
        weekFilterSelected = false;
        moodFilterSelected = false;
        reasonFilterSelected = false;

        //check if the date filter is selected
        filterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterDate.isChecked()) {
                    dateFilterSelected = true;
                } else {
                    dateFilterSelected = false;
                }
            }
        });

        //check if the week filter is selected
        filterWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterWeek.isChecked()) {
                    weekFilterSelected = true;
                } else {
                    weekFilterSelected = false;
                }
            }
        });

        // check which mood filter is selected
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.moodsspinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.moodsspinnerarray, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerText = spinner.getItemAtPosition(position).toString();
                if (spinnerText.equals("None")){
                    moodFilterSelected = false;
                }
                else {
                    moodFilterSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //apply the filters now
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reload the list first
                reloadList();

                //applies date filter
                if (dateFilterSelected) {
                    if (moodEventsHistory.size() > 0) {
                        Collections.sort(moodEventsHistory, new CustomComparator());
                    }
                }

                //applies week filter
                if (weekFilterSelected) {

                    Date myDate = new Date();
                    Date newDate = new Date(myDate.getTime() - 604800000L);

                    for (MoodEvent mood : moodEventsHistory) {
                        if (moodEventsHistory.size() > 0) {
                            if (mood.getDate().before(newDate)) {
                                moodEventsHistory.remove(mood);
                            }
                        }
                    }
                }

                //applies mood filter
                if (moodFilterSelected) {
                    for (MoodEvent mood : moodEventsHistory) {
                        if (moodEventsHistory.size() > 0) {
                            if (!mood.getMood().toString().equals(spinnerText)) {
                                    moodEventsHistory.remove(mood);
                            }
                        }
                    }
                }

                //check if reason filter is selected and apply
                if (filterReason.getText().length() != 0) {
                    reasonFilterSelected = true;
                } else {
                    reasonFilterSelected = false;
                }

                if (reasonFilterSelected) {
                    for (MoodEvent mood : moodEventsHistory) {
                        if (moodEventsHistory.size() > 0) {
                            if (!mood.getTrigger().toLowerCase().contains(filterReason.getText())) {
                                moodEventsHistory.remove(mood);
                            }
                        }
                    }
                }

                //update the listview
                adapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new ArrayAdapter<MoodEvent>(getActivity(), R.layout.item_history, moodEventsHistory);
        moodEventsListView.setAdapter(adapter);
    }

    //reload the list
    public void reloadList() {
        moodEventsHistory.clear();
        for (MoodEvent moods : ParticipantSingleton.getInstance().getSelfParticipant().getMoodList()) {
            moodEventsHistory.add(moods);
        }
    }
}
