package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 *  Created by Hussain Khan     Modified by jyuen1
 *  HistoryTabFragment is the History tab that can be
 *  seen from SelfNewsFeedActivity. It shows a list view
 *  of all the Mood Events created. From this page, you
 *  can apply one or more filters to search for a particular
 *  Mood Event
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.EditMoodActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.ViewMoodEventActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.CustomComparator;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.DeleteMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

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
                refresh();
            }
        });


        // enable viewing mood event on tap of list item
        moodEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewMoodEventActivity.class);
                intent.putExtra("moodEventId", filteredMoodList.get(position).getId());
                startActivity(intent);
            }
        });


        // enable edit and delete options on long click of a list item
        registerForContextMenu(moodEventsListView);


        return rootView;
    }

    // Taken from https://developer.android.com/guide/topics/ui/menus.html
    // on 3/17/17

    /**
     * Called when the participant long clicks on a mood event in the list
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, menu);
    }

    /**
     * Called when the participants selects an item in the context menu
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(getActivity(), EditMoodActivity.class);
                intent.putExtra("moodEventId", filteredMoodList.get(info.position).getId());
                startActivity(intent);
                return true;
            case R.id.delete:
                MoodEvent moodEventToBeRemoved = filteredMoodList.get(info.position);
                DeleteMoodController.remove(moodEventToBeRemoved);
                filteredMoodList.remove(moodEventToBeRemoved);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //TODO USE THIS for filtered mapview
    private void filter() {
        filteredMoodList.clear();

        // Check which filters have been selected
        checkFilterSelected();

        // elastic version of filter by reason
        /*
        String selfName = ParticipantSingleton.getInstance().getSelfParticipant().getLogin();
        ElasticMoodController.FilterMoodByReasonTask filter = new ElasticMoodController.FilterMoodByReasonTask();
        try {
            filter.execute(selfName, "test");
            ArrayList<MoodEvent> results = filter.get();
            for (MoodEvent event : results) {
                filteredMoodList.add(event);
            }
        } catch (Exception e) {
            Log.i("Failed", "Failed filter");
        }
        */

        // offline version
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
        dateFilterSelected = (filterDate.isChecked());

        // Check if the week filter is selected
        weekFilterSelected = (filterWeek.isChecked());

        // Check if trigger filter is selected
        triggerFilterSelected = (!filterTrigger.getText().toString().
                equals(""));

        // Check if mood is selected in mood filter
        moodFilterSelected = (!moodSpinner.getSelectedItem().toString().
                equals("None"));

    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new ArrayAdapter<>(getActivity(), R.layout.item_history, filteredMoodList);
        moodEventsListView.setAdapter(adapter);

        // initial filter according to users last settings
        refresh();
    }

    /**
     * filter and refresh the display
     */
    private void refresh() {
        filter();
        adapter.notifyDataSetChanged();
    }
}
