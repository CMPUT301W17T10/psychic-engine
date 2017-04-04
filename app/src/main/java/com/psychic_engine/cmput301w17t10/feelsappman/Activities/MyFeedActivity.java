package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.CustomComparator;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.LazyAdapter;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Commented by Alex Dong 04-03-2017
 */

/**
 * MyFeedActivity contains the functionality of filtering with your mood following list. The most
 * recent mood events of your following will be displayed under a list view. Navigation to a map
 * that shows your current location and potentially nearby mood events that have been created with
 * a location.
 */
public class MyFeedActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner menuSpinner;
    private ArrayList<MoodEvent> followingMoodsArray;
    private ArrayList<String> followingArray;
    private ListView myFeedList;
    private LazyAdapter adapter;
    private Participant participant;
    private Participant following;
    private Button maps;

    private ArrayList<MoodEvent> filteredMoodList;

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
    private final long ONEWEEK = 604800000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed);

        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMyFeed);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Feed");

        myFeedList = (ListView) findViewById(R.id.listViewMyFeed);
        maps = (Button) findViewById(R.id.maps);

        filterDate = (CheckBox) findViewById(R.id.recentfilter1);
        filterWeek = (CheckBox) findViewById(R.id.weekfilter1);
        filterTrigger = (EditText) findViewById(R.id.filterreason1);
        applyFilters = (Button) findViewById(R.id.applyfilters1);
        moodSpinner = (Spinner) findViewById(R.id.moodsspinner1);

        followingMoodsArray = new ArrayList<MoodEvent>();

        followingArray = new ArrayList<String>();

        participant = instance.getSelfParticipant();

        followingArray = participant.getFollowing();
        for (String followerName : followingArray) {
            ElasticParticipantController.FindParticipantTask fpt1 = new ElasticParticipantController.FindParticipantTask();
            fpt1.execute(followerName);

            try {
                following = fpt1.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            followingMoodsArray.add(following.getMostRecentMoodEvent());
        }

        Collections.sort(followingMoodsArray, new CustomComparator());

        filteredMoodList = new ArrayList<MoodEvent>(followingMoodsArray);

        initializeSpinner();

        // Spinner drop down elements
        List<String> moodCategories = new ArrayList<String>();
        moodCategories.add("None");     // default option
        MoodState[] moodStates = MoodState.values();
        for (MoodState moodState : moodStates) {
            moodCategories.add(moodState.toString());
        }

        // Set adapter for spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, moodCategories);
        moodSpinner.setAdapter(adapterSpinner);

        //apply the filters now
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFeedActivity.this,FollowingMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("moodEventLists",filteredMoodList);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        myFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyFeedActivity.this, ViewMoodEventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("moodEvent",followingMoodsArray.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        adapter = new LazyAdapter(this, filteredMoodList);
        myFeedList.setAdapter(adapter);
    }

    /**
     * Initializes the spinner entries so that you would be able to navigate else where in the app.
     */
    public void initializeSpinner(){
        //initalize menu items for spinner
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("My Feed");
        menuItems.add("My Profile");
        menuItems.add("Followers");
        menuItems.add("Following");
        menuItems.add("Follower Requests");

        menuSpinner = (Spinner) (findViewById(R.id.spinnerMyFeed));

        //set adapter for spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, menuItems);
        menuSpinner.setAdapter(adapterSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set default spinner item to current activity
        int spinnerPosition = adapterSpinner.getPosition("My Feed");
        menuSpinner.setSelection(spinnerPosition);

        //set onclick for spinner
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "My Profile":
                        Intent myProfileActivity = new Intent(MyFeedActivity.this, MyProfileActivity.class);
                        startActivity(myProfileActivity);
                        break;
                    case "Followers":
                        Intent followersActivity = new Intent(MyFeedActivity.this, FollowersActivity.class);
                        startActivity(followersActivity);
                        break;
                    case "Following":
                        Intent followingActivity = new Intent(MyFeedActivity.this, FollowingActivity.class);
                        startActivity(followingActivity);
                        break;
                    case "Follower Requests":
                        Intent followerRequestActivity = new Intent(MyFeedActivity.this, FollowRequestActivity.class);
                        startActivity(followerRequestActivity);
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * This method is used to determine the mood events that satisfies the filter that the participant
     * has set. The application will be able to filter locally without the use of an internet
     * connection.
     */
    private void filter() {
        filteredMoodList.clear();

        // Check which filters have been selected
        checkFilterSelected();

        // offline version
        for (MoodEvent moodEvent : followingMoodsArray) {

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
                    new Date(new Date().getTime() - ONEWEEK)))
                satisfiesDate = false;

            // add mood event to the list to be displayed if it satisfies all conditions
            if (satisfiesMood && satisfiesDate && satisfiesTrigger)
                filteredMoodList.add(moodEvent);
        }

        // sort mood events in reverse chronological order
        if (dateFilterSelected)
            Collections.sort(filteredMoodList, new CustomComparator());
    }

    /**
     * This method determines what filters have been selected by the participant to be utilized.
     */
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

    /**
     * Refresh the display every time the filter method has been called. This is to display the
     * filtered mood list under the requirements that has been set.
     */
    private void refresh() {
        filter();
        adapter.notifyDataSetChanged();
    }
}
