package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyFeedActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner menuSpinner;
    private ArrayList<MoodEvent> followingMoodsArray;
    private ArrayList<String> followingArray;
    private ListView myFeedList;
    private ArrayAdapter<MoodEvent> adapter;
    private Participant participant;
    private Participant following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed);

        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMyFeed);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Feed");

        myFeedList = (ListView) findViewById(R.id.listViewMyFeed);

        followingMoodsArray = new ArrayList<MoodEvent>();
        followingArray = new ArrayList<String>();

        ElasticParticipantController.FindParticipantTask fpt = new ElasticParticipantController.FindParticipantTask();
        fpt.execute(ParticipantSingleton.getInstance().getSelfParticipant().getLogin());

        try {
            participant = fpt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
            for (MoodEvent moodEvent : following.getMoodList()) {
                followingMoodsArray.add(moodEvent);
            }
        }



        initializeSpinner();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        adapter = new ArrayAdapter<MoodEvent>(this, R.layout.myfeed_item, followingMoodsArray);
        myFeedList.setAdapter(adapter);
    }

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
}
