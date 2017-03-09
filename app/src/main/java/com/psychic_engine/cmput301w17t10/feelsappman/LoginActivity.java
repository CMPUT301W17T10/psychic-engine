package com.psychic_engine.cmput301w17t10.feelsappman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

// created by Alex Dong | March 6, 2017

public class LoginActivity extends AppCompatActivity {
    // View has a controller
    // Controller has a model
    // Model implement Abstract observable
/*
 Temporarily have a file that would save the participant names that have signed up
 loadFromFile()
 saveFromFile()
 ArrayList<Participant> participants
  */

    private static final String FILENAME = "file.sav";
    private EditText participantEditText;
    private Button loginButton;
    private Button signupButton;
    private ParticipantSingleton instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        participantEditText = (EditText) findViewById(R.id.nameEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String participantName = participantEditText.getText().toString();


                if (instance.participantNameTaken(participantName)) {
                    instance.setSelfParticipant(instance.searchParticipant(participantName));
                    Intent intent = new Intent(LoginActivity.this, SelfNewsFeedActvity.class);
                    intent.putExtra("username",participantName);
                    //intent.putExtra("location",location);
                    //intent.putExtra("realname",realname);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this,
                            "This participant does not exist, please sign up"
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        // signup button does not take participant to a signup activity (UML) - alex
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                String participantName = participantEditText.getText().toString();
                if (ParticipantSingleton.participantNameTaken(participantName)) {
                    Toast.makeText(LoginActivity.this, "The username is already taken",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if (ParticipantSingleton.getInstance().addParticipant(participantName)) {
                        Toast.makeText(LoginActivity.this, participantName
                                +" has been added!", Toast.LENGTH_SHORT).show();
                        for (Participant participant : instance.getParticipantList()) {
                            Log.i("PARTICIPANT", participant.getLogin() + "| Size" + instance.getParticipantCount());
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Unable to add participant",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(LoginActivity.this, "Entering onStart Method", Toast.LENGTH_SHORT).show();
        if (ParticipantSingleton.isLoaded() == null) {
            Toast.makeText(LoginActivity.this, "Instance is null, attempting to load from file",
                    Toast.LENGTH_SHORT).show();
            loadFromFile();
        }
    }
    // TODO: GSON does not properly load files. Will crash the application or cause incosistencies
    // TODO: Need to change format of the GSON file
    // TODO: Temporary Solution: Clear data on your disk before running the program
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html Jan-21-2016
            Type type = new TypeToken<ParticipantSingleton>() {}.getType();
            System.out.println("BUFFEREDREADER " + gson.fromJson(in,type));
            instance = gson.fromJson(in, type);
            if (instance != null)
                instance.setInstance(instance);
            else
                instance = ParticipantSingleton.getInstance();
        } catch (FileNotFoundException e) {
            instance = ParticipantSingleton.getInstance();
        }

    }
    // TODO: Does not crash with onPause() override method, but will not saveInFile
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(ParticipantSingleton.getInstance(), out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInFile();
    }
    @Override
    protected void onStop() {
        super.onStop();
        saveInFile();
    }
}
