package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.List;

public class FollowRequestActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner menuSpinner;
    private ListView followerRequestsList;
    private ArrayList<String> followerRequestArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFollowerRequests);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Follower Requests");

        followerRequestsList = (ListView) findViewById(R.id.listViewFollowerRequests);
        registerForContextMenu(followerRequestsList);
        followerRequestArray = new ArrayList<String>();

        followerRequestArray.add("rando");
        followerRequestArray.add("dest");


        initializeSpinner();
    }

    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listViewFollowerRequests) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_handlefollow, menu);
        }
    }

    //long click for editing and deleting
    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.acceptfollow:
                followerRequestArray.remove(info.position);
                setResult(RESULT_OK);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.rejectfollow:
                followerRequestArray.remove(info.position);
                setResult(RESULT_OK);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        adapter = new ArrayAdapter<String>(this, R.layout.follower_handleitem, followerRequestArray);
        followerRequestsList.setAdapter(adapter);
    }

    public void initializeSpinner(){
        //initalize menu items for spinner
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("My Feed");
        menuItems.add("My Profile");
        menuItems.add("Followers");
        menuItems.add("Following");
        menuItems.add("Follower Requests");

        menuSpinner = (Spinner) (findViewById(R.id.spinnerFollowerRequests));

        //set adapter for spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, menuItems);
        menuSpinner.setAdapter(adapterSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set default spinner item to current activity
        int spinnerPosition = adapterSpinner.getPosition("Follower Requests");
        menuSpinner.setSelection(spinnerPosition);

        //set onclick for spinner
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "My Feed":
                        Intent myFeedActivity = new Intent(FollowRequestActivity.this, MyFeedActivity.class);
                        startActivity(myFeedActivity);
                        break;
                    case "My Profile":
                        Intent myProfileActivity = new Intent(FollowRequestActivity.this, MyProfileActivity.class);
                        startActivity(myProfileActivity);
                        break;
                    case "Followers":
                        Intent followersActivity = new Intent(FollowRequestActivity.this, FollowersActivity.class);
                        startActivity(followersActivity);
                        break;
                    case "Following":
                        Intent followingActivity = new Intent(FollowRequestActivity.this, FollowingActivity.class);
                        startActivity(followingActivity);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
