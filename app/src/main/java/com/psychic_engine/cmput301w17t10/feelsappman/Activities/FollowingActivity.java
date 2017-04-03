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
import android.widget.Button;
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
 * The FollowingActivity allows a function where you would be able to search for other users of the
 * app (exact name). Upon searching for the participant you would like to follow, you would be able
 * to submit a follow request. This request would be sent to the other participant and they would be
 * able to accept or decline your request. If they do choose to accept your request. The list show
 * on the bottom of the activity displays the most recent mood event of your following participants.
 */
public class FollowingActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner menuSpinner;
    private ListView followingList;
    private Button search;
    private ArrayList<String> followingArray;
    private ArrayList<String> followingFollowArray;
    private ArrayAdapter<String> adapter;
    private Participant participant;
    private Participant following;
    private Button maps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFollowing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Following");
        search = (Button) findViewById(R.id.search);
        maps = (Button) findViewById(R.id.maps);

        followingList = (ListView) findViewById(R.id.listViewFollowing);
        registerForContextMenu(followingList);

        followingArray = new ArrayList<String>();
        followingFollowArray = new ArrayList<String>();

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

        followingArray = participant.getFollowing();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FollowingActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listViewFollowing) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_stopfollowing, menu);
        }
    }

    /**
     * You would be able to delete following participants, thus severing the relationship between
     * follower and following
     * @param item choice
     * @return true if successful execution of item operation
     */
    //long click for editing and deleting
    //http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.stopfollowing:
                ElasticParticipantController.FindParticipantTask fpt1 = new ElasticParticipantController.FindParticipantTask();
                fpt1.execute(followingArray.get(info.position));

                try {
                    following = fpt1.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                followingFollowArray = following.getFollowers();
                followingFollowArray.remove(participant.getLogin());
                followingArray.remove(info.position);

                ElasticParticipantController.UpdateParticipantTask upt = new ElasticParticipantController.UpdateParticipantTask();
                upt.execute(participant);

                ElasticParticipantController.UpdateParticipantTask upt1 = new ElasticParticipantController.UpdateParticipantTask();
                upt1.execute(following);

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
        adapter = new ArrayAdapter<String>(this, R.layout.follower_item, followingArray);
        followingList.setAdapter(adapter);
    }

    /**
     * Initializes a spinner to allow flexibility in navigating throughout your UI.
     */
    public void initializeSpinner(){
        //initalize menu items for spinner
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("My Feed");
        menuItems.add("My Profile");
        menuItems.add("Followers");
        menuItems.add("Following");
        menuItems.add("Follower Requests");

        menuSpinner = (Spinner) (findViewById(R.id.spinnerFollowing));

        //set adapter for spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, menuItems);
        menuSpinner.setAdapter(adapterSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set default spinner item to current activity
        int spinnerPosition = adapterSpinner.getPosition("Following");
        menuSpinner.setSelection(spinnerPosition);

        //set onclick for spinner
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "My Feed":
                        Intent myFeedActivity = new Intent(FollowingActivity.this, MyFeedActivity.class);
                        startActivity(myFeedActivity);
                        break;
                    case "My Profile":
                        Intent myProfileActivity = new Intent(FollowingActivity.this, MyProfileActivity.class);
                        startActivity(myProfileActivity);
                        break;
                    case "Followers":
                        Intent followersActivity = new Intent(FollowingActivity.this, FollowersActivity.class);
                        startActivity(followersActivity);
                        break;
                    case "Follower Requests":
                        Intent followerRequestActivity = new Intent(FollowingActivity.this, FollowRequestActivity.class);
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
