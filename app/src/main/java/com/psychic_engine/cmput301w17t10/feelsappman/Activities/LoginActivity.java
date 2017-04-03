package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;


/**
 * LoginActivity is the login page of the app, and the first activity that will run upon opening
 * the app. The person using the app will be able to sign up and log in here. A username will need
 * to be given in order to log in so long it already exists (ie. signed up before). Names that have
 * not been taken will need to be signed up before logging on. Names that have been taken will not
 * be able to sign up (unique name). Upon logging in, the person will be directed to their news feed
 * (ie. profile screen).
 * @see MyProfileActivity
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
        ParticipantController.updateSingletonList();

        // clear local lists in case of offline behaviour
        instance.getOfflineCreatedList().clear();
        instance.getOfflineDeleteList().clear();

        /**
         * A login button action is used to pull the name from the EditText given in the activity.
         * The program will then check that the username has been taken already (ie. stored) as well
         * as searching the list of participants to set as the self participant. The system will then
         * direct the user to their own profile page. If the name does not exist (ie. not stored),
         * then the program will prompt the user that the name does not exist and they should sign up.
         * Note that the text used to find a participant within the server is case insensitive.
         * @see MyProfileActivity
         */
        // TODO: Update the mood list of the person logging in
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String participantName = participantEditText.getText().toString();
                if (participantName.matches("")) {
                    Toast.makeText(LoginActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!ParticipantController.checkUniqueParticipant(participantName)) {
                        try {
                            Participant self = instance.searchParticipant(participantName);
                            instance.setSelfParticipant(self);

                            // test - print out all users that were in the singleton list
                            for (Participant stored : instance.getParticipantList()) {
                                Log.i("Stored", "Stored login: " + stored.getLogin());
                            }
                            // test - print out all moods connected to the participant... should be in sync
                            for (MoodEvent storedMoods : instance.getSelfParticipant().getMoodList()) {
                                Log.i("Stored", "Stored mood ID " + storedMoods.getMood().getMood());
                            }
                            Intent intent = new Intent(LoginActivity.this, MyFeedActivity.class);
                            Log.i("Logging", "Logging in as " + instance.getSelfParticipant().getLogin());
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.i("Error", "Error logging in");
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "This participant does not exist, please sign up"
                                , Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

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
                String participantName = participantEditText.getText().toString();
                if (participantName.matches("")) {
                    Toast.makeText(LoginActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (ParticipantController.checkUniqueParticipant(participantName)) {
                        Participant newParticipant = new Participant(participantName);

                        // add participant into the server
                        ElasticParticipantController.AddParticipantTask addParticipantTask = new
                                ElasticParticipantController.AddParticipantTask();
                        try {
                            addParticipantTask.execute(newParticipant);
                            String newId = addParticipantTask.get();
                            newParticipant.setId(newId);
                            Log.i("New", "New Participant signed up as "+ newParticipant.getId());
                        } catch (Exception e) {
                            Log.i("Fail", "Failed assigning index");
                        }

                        ElasticParticipantController.UpdateParticipantTask upt = new ElasticParticipantController.UpdateParticipantTask();
                        upt.execute(newParticipant);


                        // add participant into the singleton locally
                        instance.addParticipant(newParticipant);
                        instance.setSelfParticipant(newParticipant);

                        // confirm creation message
                        Toast.makeText(LoginActivity.this, participantName
                                + " has been added!", Toast.LENGTH_SHORT).show();

                        // move user to news feed activity
                        Intent intent = new Intent(LoginActivity.this, MyFeedActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Unable to sign up as " + participantName
                                , Toast.LENGTH_SHORT).show();
                    }
                }
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
