package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.CreateMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.R;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

import java.util.ArrayList;
import java.util.List;

import static com.psychic_engine.cmput301w17t10.feelsappman.R.id.imageView;
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
    private static int RESULT_LOAD_IMAGE = 1;

    private Spinner moodSpinner;
    private Spinner socialSettingSpinner;
    private EditText triggerEditText;
    private CheckBox locationCheckBox; // TODO: change type
    private Button browseButton;
    private ImageView photoImageView;
    private Button createButton;
    private Button cancelButton;
    private CreateMoodController createMoodController = new CreateMoodController();
    /**
     * Calls upon the methods to initialize the UI needed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoImageView = (ImageView) findViewById(R.id.imageView);
        setContentView(R.layout.activity_create_mood);

        //List of permissions required and Requestcode for the permssions needed
        //in ActivityCompat.requestPermissions
        int permission_code = 1;
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, permission_code);
        }

        // set up mood and social setting spinners (drop downs)
        setUpSpinners();

        // set up events that happen when user clicks in trigger and outside trigger
        setUpTrigger();

        // set up events that happen when user clicks browse button
        setUpBrowse();

        // set up events that happen when user clicks location button
        setUpLocation();

        // set up events that happen when user clicks create button
        setUpCreate();

        // set up events that happen when user clicks cancel button
        setUpCancel();
    }
    //Taken from http://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
    //March 26, 2017

    /**
     * Method to detect whether or not permissions required for the app to run are granted. Upon
     * earlier versions of the SDK, permission is automatically granted
     * @return true if SDK < 23 or participant permits
     * @return false if participant denies and SDK > 23
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23 && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
    public void createMoodEvent() {
        Log.d("Clicked on Create", "MoodEvent should be added here");
        String moodString = moodSpinner.getSelectedItem().toString();
        String socialSettingString = socialSettingSpinner.getSelectedItem().toString();
        String trigger = triggerEditText.getText().toString();

        //optional features that require a model initially set to null
        Photograph photo = null;
        MoodLocation location = null;

        boolean photoSizeUnder = TRUE;


        //Taken from http://stackoverflow.com/questions/26865787/get-bitmap-from-imageview-in-android-l
        //March 10, 2017
        //gets drawable from imageview and converts drawable to bitmap

        try {
            Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
            photo = new Photograph(bitmap);
            photoSizeUnder = photo.getLimitSize();
        } catch (Exception e) {
            // pass
        }

        //String location = locationEditText.getText().toString(); // TODO tentative, location type will change in part 5



        if (photoSizeUnder) {
            boolean success = createMoodController.updateMoodEventList(moodString, socialSettingString, trigger, photo, location);

            if (!success) {
                Toast.makeText(CreateMoodActivity.this,
                        "Please specify a mood.",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(CreateMoodActivity.this, SelfNewsFeedActivity.class);
                startActivity(intent);
            }

        } else {
            Toast.makeText(CreateMoodActivity.this,
                    "Photo size is too large! (Max 65536 bytes)",
                    Toast.LENGTH_LONG).show();
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
        moodCategories.add("Select a mood");     // default option
        MoodState[] moodStates = MoodState.values();
        for (MoodState moodState : moodStates) {
            moodCategories.add(moodState.toString());
        }

        List<String> socialSettingCategories = new ArrayList<String>();
        socialSettingCategories.add("Select a social setting");    // default option
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
    }

    /**
     * Setup method for the location EditText (TEMPORARY) category
     */
    void setUpLocation() {
        locationCheckBox = (CheckBox) findViewById(R.id.includeLocation);

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



    @Override
    protected void onPause() {
        super.onPause();
        FileManager.saveInFile(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FileManager.saveInFile(this);
    }

}
