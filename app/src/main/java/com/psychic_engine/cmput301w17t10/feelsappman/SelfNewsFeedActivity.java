package com.psychic_engine.cmput301w17t10.feelsappman;

import android.content.Intent;
import android.icu.text.MessagePattern;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

//SelfNewsFeedActivity named, I dont know which activity it corresponds to in the UML -Alex

public class SelfNewsFeedActivity extends AppCompatActivity {
    private ArrayList<Participant> selfParticipant;
    private Participant singleParticipant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // testing CreateMoodActivity
        //Intent intent = new Intent(this, CreateMoodActivity.class);
        //startActivity(intent);

        Gson gsonIn = new Gson();
<<<<<<< HEAD
        String gsonObject = getIntent().getStringExtra("participantListObjects");
        Type listType = new TypeToken<ArrayList<Participant>>() {}.getType();
        selfParticipant = gsonIn.fromJson(gsonObject, listType);
        System.out.println(selfParticipant);
=======
        Intent intentIn = getIntent();
        Type listType = new TypeToken<ArrayList<Participant>>(){}.getType();
        String gsonObject = getIntent().getStringExtra("participantListObjects");
        selfParticipant = gsonIn.fromJson(gsonObject, listType);
>>>>>>> be1cb7afa1ffc7056e0f7138dbbd81caae0beb26

    }
}
