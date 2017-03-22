package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticSearchController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.concurrent.ExecutionException;

// created by Alex Dong | March 6, 2017 | Comments by Alex Dong

/**
 * LoginActivity is the login page of the app, and the first activity that will run upon opening
 * the app. The person using the app will be able to sign up and log in here. A username will need
 * to be given in order to log in so long it already exists (ie. signed up before). Names that have
 * not been taken will need to be signed up before logging on. Names that have been taken will not
 * be able to sign up (unique name). Upon logging in, the person will be directed to their news feed
 * (ie. profile screen).
 * @see SelfNewsFeedActivity
 * @see ParticipantSingleton
 * @see Participant
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * FILENAME will be the current file the app will store the names with. Later on, a remote
     * server will be used to save these names through elastic search. A participant singleton is
     * used to store the participant information. It is only currently used to store all names
     * that have registered in the app and setting the self participant to the person's name
     *
     * @see ParticipantSingleton
     */
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

        loadInstance();
        instance = ParticipantSingleton.getInstance();

        /**
         * A login button action is used to pull the name from the EditText given in the activity.
         * The program will then check that the username has been taken already (ie. stored) as well
         * as searching the list of participants to set as the self participant. The system will then
         * direct the user to their own profile page. If the name does not exist (ie. not stored),
         * then the program will prompt the user that the name does not exist and they should sign up.
         * @see SelfNewsFeedActivity
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String participantName = participantEditText.getText().toString();
                if (!ParticipantController.checkUniqueParticipant(participantName)) {
                    Participant self = null;
                    try {
                        ElasticSearchController.FindParticipantTask findParticipantTask = new
                                ElasticSearchController.FindParticipantTask();
                        self = findParticipantTask.execute(participantName).get();
                        ParticipantSingleton.getInstance().setSelfParticipant(self);
                        Intent intent = new Intent(LoginActivity.this, SelfNewsFeedActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.i("Error", "Error logging in");
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this,
                            "This participant does not exist, please sign up"
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });

        // signup button does not take participant to a signup activity (UML) - alex
        /**
         * The signup button action will cause the system to store the name that was given in the
         * EditText and thus be "registered" into the system. The system would then be able to store
         * all of the detail os that specific participant upon logging in. Upon successful addition,
         * the system will prompt that the participant name has been added and the user would be
         * able to log in under that name. The system will also be able to prompt the user whether
         * or not the entry was valid or not (ie. empty text).
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                String participantName = participantEditText.getText().toString();
                Participant newParticipant = new Participant(participantName);
                if (ParticipantController.checkUniqueParticipant(participantName)) {
                    ParticipantSingleton.getInstance().addParticipant(participantName);
                    ElasticParticipantController.AddParticipantTask addParticipantTask = new
                            ElasticParticipantController.AddParticipantTask();
                    addParticipantTask.execute(newParticipant);
                    Toast.makeText(LoginActivity.this, participantName
                            + " has been added!", Toast.LENGTH_SHORT).show();
                    Participant self = instance.searchParticipant(participantName);
                    // TODO: Check for new participant logged user and add him
                    instance.setSelfParticipant(self);
                    Intent intent = new Intent(LoginActivity.this, SelfNewsFeedActivity.class);
                    startActivity(intent);
                }

                /*
                if (!ElasticSearchController.takenName(participantName)) {
                    ParticipantSingleton.getInstance().addParticipant(participantName);
                    ElasticParticipantController.AddParticipantTask addParticipantTask = new
                            ElasticParticipantController.AddParticipantTask();
                    addParticipantTask.execute(newParticipant);
                    Toast.makeText(LoginActivity.this, participantName
                            + " has been added!", Toast.LENGTH_SHORT).show();
                    Participant self = instance.searchParticipant(participantName);
                    instance.setSelfParticipant(self);
                    Intent intent = new Intent(LoginActivity.this, SelfNewsFeedActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Input invalid, please try again",
                            Toast.LENGTH_SHORT).show();
                    }
                    */
            }
        });
    }

    /**
     * Setting the instance up such that if was no instance to begin with (ie. first time starting),
     * then the program will call loadFromFile to get the instance that was previously saved, thus
     * acquiring the details from the previous execution.
     */
    private void loadInstance() {
        if (ParticipantSingleton.isLoaded() == null) {
            Toast.makeText(LoginActivity.this, "Instance is null, attempting to load from file",
                    Toast.LENGTH_SHORT).show();
            FileManager.loadFromFile(this);
        }
    }


    /**
     * When the program is on pause (leaving or idling activity), the saveInFile() method will run,
     * thus saving all the changes that have been made.
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileManager.saveInFile(this);
    }

    /**
     * Similar to the onPause() method, upon destruction by closing the app, the saveInFile()
     * method will run so that when the app opens again, it can obtain all of the information that
     * was saved.
     */
    @Override
    protected void onStop() {
        super.onStop();
        FileManager.saveInFile(this);
    }
}
