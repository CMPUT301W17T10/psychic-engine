package com.psychic_engine.cmput301w17t10.feelsappman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import static android.app.Activity.RESULT_OK;

public class LoginActivity extends AppCompatActivity {
/*
 Temporarily have a file that would save the participant names that have signed up
 loadFromFile()
 saveFromFile()
 ArrayList<Participant> participants
  */
    private static final String FILENAME = "file.sav";
    private ArrayList<Participant> participantList;
    private EditText participantEditText;
    private Button loginButton;
    private Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        participantEditText = (EditText) findViewById(R.id.nameEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                String participantName = participantEditText.getText().toString();
                if (!participantList.contains(participantName)) {
                    Toast.makeText(LoginActivity.this,
                            "This participant does not exist, please sign up"
                            ,Toast.LENGTH_LONG).show();
                }
                int position = participantList.indexOf(participantName);
                Participant participant = participantList.get(position);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("participant", participantName);


            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                String participantName = participantEditText.getText().toString();
                if (!participantList.contains(participantName)) {
                    Participant newParticipant = new Participant(participantName);
                    participantList.add(newParticipant);
                    saveInFile();
                    Toast.makeText(LoginActivity.this, "New participant created!"
                            , Toast.LENGTH_LONG).show();
                }
                Toast.makeText(LoginActivity.this, "This participant has already signed up",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFromFile();

    }
    private void loadFromFile() {

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html Jan-21-2016
            Type listType = new TypeToken<ArrayList<Participant>>() {}.getType();
            participantList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            participantList = new ArrayList<Participant>();
        }

    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(participantList, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
