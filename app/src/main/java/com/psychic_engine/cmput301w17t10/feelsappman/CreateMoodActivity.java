package com.psychic_engine.cmput301w17t10.feelsappman;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jyuen1 on 3/6/17.
 */

public class CreateMoodActivity extends AppCompatActivity {
    private static final String defaultTriggerMsg = "20 chars or 3 words.";
    private static int RESULT_LOAD_IMAGE = 1;

    private Spinner moodSpinner;
    private Spinner socialSettingSpinner;
    private EditText triggerEditText;
    private EditText locationEditText; // TODO: change type
    private Button browseButton;
    private ImageView photoImageView;
    private Button createButton;
    private Button cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);

        // set up mood and social setting spinners (drop downs)
        setUpSpinners();
        // set up events that happen when user clicks in trigger and outside trigger
        setUpTrigger();
        // set up events that happen when user clicks browse button
        setUpBrowse();
        // set up events that happen when user clicks create button
        setUpCreate();
        // set up events that happen when user clicks cancel button
        setUpCancel();
    }


    void createMoodEvent() {
        // get the mood from the mood spinner
        String moodString = moodSpinner.getSelectedItem().toString();
        Mood mood = null;

        switch(moodString) {        // TODO refactor this - inside MoodState enum class?
            case "Sad": mood = new Mood(MoodState.SAD); break;
            case "Happy": mood = new Mood(MoodState.HAPPY); break;
            case "Shame": mood = new Mood(MoodState.SHAME); break;
            case "Fear": mood = new Mood(MoodState.FEAR); break;
            case "Anger": mood = new Mood(MoodState.ANGER); break;
            case "Surprised": mood = new Mood(MoodState.SURPRISED); break;
            case "Disgust": mood = new Mood(MoodState.DISGUST); break;
            case "Confused": mood = new Mood(MoodState.CONFUSED); break;
            default:
                Toast.makeText(CreateMoodActivity.this,
                        "Please specify a mood.",
                        Toast.LENGTH_LONG).show();
                return;
        }

        // get the trigger from the trigger edit text
        String trigger = triggerEditText.getText().toString();
        if (trigger.equals(defaultTriggerMsg))
            trigger = "";

        Photograph photo = null; // TODO get photo from imageView but not sure what type it is
        Location location = null; // TODO get location from location box - need to know how to use GOOGLE MAPS first

        MoodEvent moodEvent = new MoodEvent(mood, trigger, photo, location);

        // TODO need a class that keeps track of the current user so i can add this mood to that users mood list
    }

    void setUpSpinners() {
        // Spinner elements
        moodSpinner = (Spinner) findViewById(R.id.moodDropDown);
        socialSettingSpinner = (Spinner) findViewById(R.id.socialSettingDropDown);

        // Spinner drop down elements
        List<String> moodCategories = new ArrayList<String>();
        moodCategories.add("");     // default option
        MoodState[] moodStates = MoodState.values();
        for (MoodState moodState : moodStates) {
            moodCategories.add(moodState.toString());
        }

        List<String> socialSettingCategories = new ArrayList<String>();
        socialSettingCategories.add("");    // default option
        SocialSetting[] socialSettings = SocialSetting.values();
        for (SocialSetting socialSetting : socialSettings) {
            socialSettingCategories.add(socialSetting.toString());
        }

        // Creating adapter for spinners
        ArrayAdapter<String> moodSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, moodCategories);
        ArrayAdapter<String> socialSettingSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, socialSettingCategories);

        // Drop down layout style - list view with radio button
        moodSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        socialSettingSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // Attaching adapter to spinner
        moodSpinner.setAdapter(moodSpinnerAdapter);
        socialSettingSpinner.setAdapter(socialSettingSpinnerAdapter);
    }

    void setUpTrigger() {
        //TODO not working perfectly - requires 2 clicks after initial click
        // clear trigger edit text when user clicks in it if default msg is displayed
        triggerEditText = (EditText) findViewById(R.id.trigger);
        triggerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triggerEditText.getText().toString().equals(defaultTriggerMsg))
                    triggerEditText.setText("");
            }
        });

        // reset trigger edit text message if the user clicks away from it and it is blank
        triggerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // user has clicked out of triggerEditText
                    if (triggerEditText.getText().toString().equals(""))
                        triggerEditText.setText(defaultTriggerMsg);
                }
            }
        });
    }

    void setUpBrowse() {
        // Taken from http://stackoverflow.com/questions/21072034/image-browse-button-in-android-activity
        // on 03-06-17
        browseButton = (Button) findViewById(R.id.browse);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    // displayed the browsed image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            photoImageView = (ImageView) findViewById(R.id.imageView);
            photoImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    void setUpCreate() {
        createButton = (Button) findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO (maybe?): createMoodController can observe when this button is pressed
                // and perform below actions for this class
                createMoodEvent();
            }
        });
    }

    void setUpCancel() {
        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
