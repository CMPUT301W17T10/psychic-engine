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

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.Follows;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.List;

public class FollowersActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private ListView followerlist;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);


        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Followers");
        followerlist = (ListView) findViewById(R.id.listView);





       // setupspinners();


    }

    /*void setupspinners(){
        spinner = (Spinner) (findViewById(R.id.dropdown1));

        List<String> managefollow = new ArrayList<String>();
        managefollow.add("");
        managefollow.add(instance.getSelfParticipant().getLogin());
        Follows[] followses = Follows.values();
        for (Follows follows : followses) {
            managefollow.add(follows.toString());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,managefollow);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        Intent intent = new Intent(.this,FollowersActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent following = new Intent(SelfNewsFeedActivity.this,FollowingActivity.class);
                        startActivity(following);
                        break;
                    case 3:
                        Intent followrequest = new Intent(SelfNewsFeedActivity.this,FollowRequestActivity.class);
                        startActivity(followrequest);
                        break;
                }
            }
            */

}
