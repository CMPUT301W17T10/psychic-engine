package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {
    private Button usersearch;
    private ListView results;
    private EditText usertext;
    private Participant participant;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String>pendinglist;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usersearch = (Button) findViewById(R.id.usersearch);
        usertext = (EditText) findViewById(R.id.userentry);
        results = (ListView) findViewById(R.id.resultslist);

        usersearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ElasticParticipantController.FindParticipantTask fpt = new ElasticParticipantController.FindParticipantTask();
                fpt.execute(usertext.getText().toString());

                try {
                    participant = fpt.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                arrayList = new ArrayList<String>();

                try {
                    arrayList.add(participant.getLogin());
                }catch (NullPointerException e) {
                    Toast.makeText(SearchActivity.this,"Participant Not Found",Toast.LENGTH_SHORT).show();

                }

                adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.item_history, arrayList);
                results.setAdapter(adapter);


                results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pendinglist = participant.getPendingRequests();

                        if(pendinglist.contains(participant.getLogin())){
                            Toast.makeText(SearchActivity.this,"Request Already Sent!",Toast.LENGTH_SHORT).show();
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(SearchActivity.this, "Request Sent!", Toast.LENGTH_SHORT).show();

                            pendinglist.add(participant.getLogin());
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
                        }

                    }
                });






            }
        });


    }



    }
