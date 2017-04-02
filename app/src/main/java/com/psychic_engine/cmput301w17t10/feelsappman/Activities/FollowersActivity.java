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

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FollowersActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner menuSpinner;
    private ListView followerList;
    private ArrayList<String> followerArray;
    private ArrayList<String> followerFollowingArray;
    private ArrayAdapter<String> adapter;
    private Participant participant;
    private Participant follower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFollowers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Followers");

        followerList = (ListView) findViewById(R.id.listViewFollowers);
        registerForContextMenu(followerList);

        followerArray = new ArrayList<String>();
        followerFollowingArray = new ArrayList<String>();

        initializeSpinner();

        ElasticParticipantController.FindParticipantTask fpt = new ElasticParticipantController.FindParticipantTask();
        fpt.execute(ParticipantSingleton.getInstance().getSelfParticipant().getLogin());

        try {
            participant = fpt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        followerArray = participant.getFollowers();

    }

    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listViewFollowers) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_unfollow, menu);
        }
    }

    //long click for editing and deleting
    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.unfollow:
                ElasticParticipantController.FindParticipantTask fpt1 = new ElasticParticipantController.FindParticipantTask();
                fpt1.execute(ParticipantSingleton.getInstance().getSelfParticipant().getLogin());

                try {
                    follower = fpt1.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                followerFollowingArray = follower.getFollowing();
                followerFollowingArray.remove(followerArray.get(info.position));
                followerArray.remove(info.position);

                ElasticParticipantController.UpdateParticipantTask upt = new ElasticParticipantController.UpdateParticipantTask();
                upt.execute(participant);

                ElasticParticipantController.UpdateParticipantTask upt1 = new ElasticParticipantController.UpdateParticipantTask();
                upt1.execute(follower);

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
        adapter = new ArrayAdapter<String>(this, R.layout.follower_item, followerArray);
        followerList.setAdapter(adapter);
    }

    public void initializeSpinner(){
        //initalize menu items for spinner
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("My Feed");
        menuItems.add("My Profile");
        menuItems.add("Followers");
        menuItems.add("Following");
        menuItems.add("Follower Requests");

        menuSpinner = (Spinner) (findViewById(R.id.spinnerFollowers));

        //set adapter for spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, menuItems);
        menuSpinner.setAdapter(adapterSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set default spinner item to current activity
        int spinnerPosition = adapterSpinner.getPosition("Followers");
        menuSpinner.setSelection(spinnerPosition);

        //set onclick for spinner
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "My Feed":
                        Intent myFeedActivity = new Intent(FollowersActivity.this, MyFeedActivity.class);
                        startActivity(myFeedActivity);
                        break;
                    case "My Profile":
                        Intent myProfileActivity = new Intent(FollowersActivity.this, MyProfileActivity.class);
                        startActivity(myProfileActivity);
                        break;
                    case "Following":
                        Intent followingActivity = new Intent(FollowersActivity.this, FollowingActivity.class);
                        startActivity(followingActivity);
                        break;
                    case "Follower Requests":
                        Intent followerRequestActivity = new Intent(FollowersActivity.this, FollowRequestActivity.class);
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



