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
    private ParticipantSingleton instance;
    private Participant singleParticipant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}
