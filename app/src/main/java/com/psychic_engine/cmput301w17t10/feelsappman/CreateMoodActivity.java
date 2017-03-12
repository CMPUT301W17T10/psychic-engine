package com.psychic_engine.cmput301w17t10.feelsappman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.psychic_engine.cmput301w17t10.feelsappman.R.id.imageView;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by jyuen1 on 3/6/17.
 * Comments by Alex Dong on 3/12/17.
 */

/**
 * CreateMoodActivity will be similar to the EditMoodActivity in such a way that the format will be
 * the same. However, one will be able to edit previously created events and one will only be able
 * to create new ones. The participant will be able to enter a variety of options where the mood
 * state is mandatory for entry, while others are optional.
 * @see EditMoodActivity
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

    /**
     * Calls upon the methods to initialize the UI needed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoImageView = (ImageView) findViewById(R.id.imageView);
        setContentView(R.layout.activity_create_mood);

        isStoragePermissionGranted();

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
    //Taken from http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow/41221852#41221852
    //March 10, 2017

    /**
     * Method to detect whether or not reading from the phone storage is enabled or disabled. Upon
     * earlier versions of the SDK, permission is automatically granted
     * @return true if SDK < 23 or participant permits
     * @return false if participant denies and SDK > 23
     */
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

    /**
     * Main method to call whenever the participant was to create their mood event after setting
     * their options. The system will obtain all of the information (null or not), and then save
     * the mood event into the participants own arrays of mood events. Depending on the options,
     * the participant will be able to save it as a picture, which will have required a prompt to
     * access the external storage.
     */
    void createMoodEvent() {
        // get the mood from the mood spinner
        String moodString = moodSpinner.getSelectedItem().toString();
        String socialSettingString = moodSpinner.getSelectedItem().toString();
        // get the trigger from the trigger edit text
        String trigger = triggerEditText.getText().toString();
        //initially sets photo to null
        Photograph photo = null;
        boolean photoSizeUnder = TRUE;

        if (photoImageView != null) {
            //Taken from http://stackoverflow.com/questions/26865787/get-bitmap-from-imageview-in-android-l
            //March 10, 2017
            //gets drawable from imageview and converts drawable to bitmap
            BitmapDrawable drawable = (BitmapDrawable) photoImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            photo = new Photograph(bitmap);
            photoSizeUnder = photo.getLimitSize();
        }

        Location location = null; // TODO get location from location box - need to know how to use GOOGLE MAPS first

        //TODO call this explicitly like this or through notifyObservers()
        if (photoSizeUnder) {
            boolean success = CreateMoodController.updateMoodEventList(moodString, socialSettingString, trigger, photo, location);

            if (!success)
                Toast.makeText(CreateMoodActivity.this,
                        "Please specify a mood.",
                        Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(CreateMoodActivity.this,
                    "Photo size is too large! (Max 65536 bytes)",
                    Toast.LENGTH_LONG).show();
        }
        //TODO: MoodEvent list for selfParticipant needs to save
        //TODO: MoodEvent list resets on app termination and reopen
        //TODO: Maybe try to get the bring in saveInFile on a superclass and keep on all activities
        for (MoodEvent mood : ParticipantSingleton.getInstance().getSelfParticipant().getMoodList()) {
            Log.i("MoodEvent Added", "This mood event is of: " + mood.getMood().getMood());
        }
    }

    /**
     * Setup method to create the spinners in the UI
     */
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

    /**
     * Setup method for the trigger EditText category
     */
    void setUpTrigger() {

        triggerEditText = (EditText) findViewById(R.id.trigger);
        triggerEditText.setText("");

        // TODO not working perfectly - requires 2 clicks after initial click
        // TODO giving me errors in test - leaving it blank for now
        // clear trigger edit text when user clicks in it if default msg is displayed
        /*
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
        */
    }

    /**
     * Setup method the browse button, being able to select pictures from the phone storage.
     */
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

    /**
     * Upon execution, the activity will be able to display the photo that the participant selected
     * so long the size is within limit.
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

            photoImageView = (ImageView) findViewById(imageView);
            photoImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    /**
     * Setup method for the create button, which will issue a command to create the mood event on
     * click.
     */
    void setUpCreate() {
        createButton = (Button) findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMoodEvent();
            }
        });
    }

    /**
     * Setup method for the cancel button, which will issue a command to close the addition of a
     * mood event if the paticipant ever changes their mind.
     */
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
