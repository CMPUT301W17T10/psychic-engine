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

/**
 * FollowRequestActivity displays the pending requests of anyone who wishes to follow your mood events.
 * You would be able to accept and decline these requests through a long click. Of course, upon
 * accepting the request, a follower-following relationship will be created and they will be able
 * to see your most recent mood event in other areas of the app.
 */
public class FollowRequestActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner menuSpinner;
    private ListView followerRequestsList;
    private ArrayList<String> followerRequestArray;
    private ArrayList<String> participantFollowArray;
    private ArrayList<String> senderFollowing;
    private ArrayAdapter<String> adapter;
    private Participant participant;
    private Participant senderParticipant;

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
        participantFollowArray = new ArrayList<String>();
        senderFollowing = new ArrayList<String>();

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

        followerRequestArray = participant.getPendingRequests();
        participantFollowArray = participant.getFollowers();
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

    /**
     * Method implements the functionality of being able to accept/decline the request through
     * a long click on the participant you would be handle.
     * @param item
     * @return
     */
    //long click for editing and deleting
    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.acceptfollow:
                ElasticParticipantController.FindParticipantTask fpt1 = new ElasticParticipantController.FindParticipantTask();
                fpt1.execute(followerRequestArray.get(info.position));

                try {
                    senderParticipant = fpt1.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                senderFollowing = senderParticipant.getFollowing();
                senderFollowing.add(participant.getLogin());

                participantFollowArray.add(followerRequestArray.get(info.position));
                followerRequestArray.remove(info.position);

                ElasticParticipantController.UpdateParticipantTask upt = new ElasticParticipantController.UpdateParticipantTask();
                upt.execute(participant);

                ElasticParticipantController.UpdateParticipantTask upt1 = new ElasticParticipantController.UpdateParticipantTask();
                upt1.execute(senderParticipant);

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

    /**
     * Initialize the spinner to create the flexibility of being able to navigate anywhere in your UI.
     */
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
