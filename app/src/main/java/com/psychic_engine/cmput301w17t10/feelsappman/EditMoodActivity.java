package com.psychic_engine.cmput301w17t10.feelsappman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
 * Created by jyuen1 on 3/7/2017.
 */

public class EditMoodActivity extends AppCompatActivity{
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

    private MoodEvent moodEvent;    // the moodEvent to be edited

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);

        isStoragePermissionGranted();

        // TODO initialize moodEvent - how are we passing it in via intent, global, index in array? or something else?
        /*
        Bundle extras = getIntent().getExtras();
        Participant participant = ParticipantSingleton.getCurrentUser();
        // singleton has an array of all participants?  login sets current user?
        int moodEventPosition = extras.getInt(CallingActivity.EXTRA_MOODEVENT_POSITION);
        moodEvent = participant.getMoodList().get(moodEventPosition);
        */

        // set up mood and social setting spinners (drop downs)
        setUpSpinners();
        // set up events that happen when user clicks in trigger and outside trigger
        setUpTrigger();
        // set up the currently displayed picture
        setUpImageView();
        // set up events that happen when user clicks browse button
        setUpBrowse();
        // set up events that happen when user clicks create button
        setUpSave();
        // set up events that happen when user clicks cancel button
        setUpCancel();
    }

    //Taken from http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow/41221852#41221852
    //March 10, 2017
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

            //resume tasks needing this permission
        }
    }

    void saveMoodEvent() {
        // get the mood from the mood spinner
        String moodString = moodSpinner.getSelectedItem().toString();

        // get the social setting from the social setting spinner
        String socialSettingString = socialSettingSpinner.getSelectedItem().toString();

        // get the trigger from the trigger edit text
        String trigger = triggerEditText.getText().toString();

        Photograph photo = null; // TODO get photo from imageView but not sure what type it is, could be some primitive type and convert to Photograph type in the controller
        Location location = null; // TODO get location from location box - need to know how to use GOOGLE MAPS first

        //TODO call this explicitly like this or through notifyObservers()
        //EditMoodController.updateMoodEventList(moodEventPosition, moodString, socialSettingString, trigger, photo, location);
    }

    void setUpSpinners() {
        // Spinner elements
        moodSpinner = (Spinner) findViewById(R.id.moodDropDown1);
        socialSettingSpinner = (Spinner) findViewById(R.id.socialSettingDropDown1);

        // Spinner drop down elements
        List<String> moodCategories = new ArrayList<String>();
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

        // set the previous mood
        moodSpinner.setSelection(moodSpinnerAdapter.getPosition(
                moodEvent.getMood().toString()));
        // set the previous social setting
        socialSettingSpinner.setSelection(socialSettingSpinnerAdapter.getPosition(
                moodEvent.getSocialSetting().toString()));

    }

    void setUpTrigger() {
        // display the previous trigger
        triggerEditText = (EditText) findViewById(R.id.trigger1);
        triggerEditText.setText(moodEvent.getTrigger());
        if (triggerEditText.getText().equals(""))
            triggerEditText.setText(defaultTriggerMsg);

        // TODO not working perfectly - requires 2 clicks after initial click
        // clear trigger edit text when user clicks in it if default msg is displayed
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

    void setUpImageView() {
        // display the previous image
        photoImageView = (ImageView) findViewById(R.id.imageView1);
        photoImageView.setImageBitmap(moodEvent.getPicture().getImage());   // TODO requires photograph class to return the correct image
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

            photoImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    void setUpSave() {
        createButton = (Button) findViewById(R.id.save);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMoodEvent();
            }
        });
    }

    void setUpCancel() {
        cancelButton = (Button) findViewById(R.id.cancel1);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
