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

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.Followers;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.Follows;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.List;

public class FollowersActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private ListView followerlist;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);


        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Followers");
        followerlist = (ListView) findViewById(R.id.listView);





       setupspinners();


    }

    void setupspinners(){
        spinner1 = (Spinner) (findViewById(R.id.dropdown1));

        List<String> followers = new ArrayList<String>();
        followers.add("");
        followers.add(instance.getSelfParticipant().getLogin());
        Followers[] followerses = Followers.values();
        for (Followers followers1 : followerses) {
            followers.add(followers1.toString());
        }



        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,followers);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Intent intent = new Intent(FollowersActivity.this, SelfNewsFeedActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent following = new Intent(FollowersActivity.this, FollowingActivity.class);
                        startActivity(following);
                        break;
                    case 3:
                        Intent followrequest = new Intent(FollowersActivity.this, FollowRequestActivity.class);
                        startActivity(followrequest);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}



