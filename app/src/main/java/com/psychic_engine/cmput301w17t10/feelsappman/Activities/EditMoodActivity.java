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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.EditMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.R;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

/**
 * Created by jyuen1 on 3/7/2017.
 */

/**
 * This class allows the user to edit mood events.
 */
public class EditMoodActivity extends AppCompatActivity{
    private static int RESULT_LOAD_IMAGE = 1;

    private Spinner moodSpinner;
    private Spinner socialSettingSpinner;
    private EditText triggerEditText;
    private CheckBox locationCheckBox; // TODO: change type
    private Button browseButton;
    private ImageView photoImageView;
    private Button createButton;
    private Button cancelButton;

    private MoodEvent moodEvent;    // the moodEvent to be edited
    private String moodEventId;

    /**
     * Called on activity creation.  Initializes widgets and class variables.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);

        //List of permissions required and Requestcode for the permssions needed
        //in ActivityCompat.requestPermissions
        int permission_code = 1;
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, permission_code);
        }

        moodEventId = getIntent().getExtras().getString("moodEventId");
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        // This is O(n) - can be improved to O(1) if we implement serializable on moodEvent instead
        // would work from recent or history even if filtered
        // since the moodEvent would be passed by reference (address)
        // but serializable is "slow and inefficient"
        for (MoodEvent m : participant.getMoodList()) {
            if (m.getId().equals(moodEventId))
                moodEvent = m;
        }

        //moodEvent = participant.getMoodList().get(moodEventPosition);

        // set up mood and social setting spinners (drop downs)
        setUpSpinners();

        // set up events that happen when user clicks in trigger
        setUpTrigger();

        // set up events that happen when user clicks in location
        setUpLocation();

        // set up the currently displayed picture
        setUpImageView();

        // set up events that happen when user clicks browse button
        setUpBrowse();

        // set up events that happen when user clicks create button
        setUpSave();

        // set up events that happen when user clicks cancel button
        setUpCancel();
    }

    /**
     * Method to detect whether or not reading from the phone storage is enabled or disabled. Upon
     * earlier versions of the SDK, permission is automatically granted
     * @return true if SDK < 23 or participant permits
     * @return false if participant denies and SDK > 23
     */
    //Taken from http://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
    //March 26, 2017
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
     * Retrieves information set in widgets and calls EditMoodController to register changes.
     * @see EditMoodController
     */
    void saveMoodEvent() {
        // get the mood from the mood spinner
        String moodString = moodSpinner.getSelectedItem().toString();

        // get the social setting from the social setting spinner
        String socialSettingString = socialSettingSpinner.getSelectedItem().toString();

        // get the trigger from the trigger edit text
        String trigger = triggerEditText.getText().toString();

        Photograph photo = null;
        MoodLocation location = null;

        boolean photoSizeUnder = TRUE;

        try {
            Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
            photo = new Photograph(bitmap);
            photoSizeUnder = photo.getLimitSize();
        } catch (Exception e) {
            // pass
        }

        //String location = locationEditText.getText().toString(); // TODO change location type in part 5

        if (photoSizeUnder) {
            try {
                EditMoodController.updateMoodEventList(
                        moodEvent, moodString, socialSettingString, trigger, photo, location);
            } catch (TriggerTooLongException e) {
                Toast.makeText(EditMoodActivity.this,
                        "Trigger must be 3 words and under 20 chars!",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(EditMoodActivity.this,
                    "Photo size is too large! (Max 65536 bytes)",
                    Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(EditMoodActivity.this, SelfNewsFeedActivity.class);
        startActivity(intent);
    }

    /**
     * Initializes and adds categories to spinners.
     */
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


        // set the previous mood
        moodSpinner.setSelection(moodSpinnerAdapter.getPosition(
                moodEvent.getMood().getMood().toString()));
        // set the previous social setting
        if (moodEvent.getSocialSetting() != null)
            socialSettingSpinner.setSelection(socialSettingSpinnerAdapter.getPosition(
                    moodEvent.getSocialSetting().toString()));
        else
            socialSettingSpinner.setSelection(0);
    }

    /**
     * Initializes trigger edit text widget
     */
    void setUpTrigger() {
        // display the previous trigger
        triggerEditText = (EditText) findViewById(R.id.trigger1);
        triggerEditText.setText(moodEvent.getTrigger());
    }

    /**
     * Initializes location edit text widget
     */
    void setUpLocation() {
        // display the previous location
        locationCheckBox = (CheckBox) findViewById(R.id.includeLocation);
        //locationCheckBox.setText(moodEvent.getLocation());
    }
    /**
     * Initializes photo image view widget
     */
    void setUpImageView() {
        // display the previous image
        photoImageView = (ImageView) findViewById(R.id.imageView1);
        if (moodEvent.getPicture() != null)
            photoImageView.setImageBitmap(moodEvent.getPicture().getImage());
    }

    /**
     * Initializes the browse button.  Launches an activity that allows the user to select 
     * an image from their photo album to set as the mood event image.
     */
    void setUpBrowse() {
        // Taken from http://stackoverflow.com/questions/21072034/image-browse-button-in-android-activity
        // on 03-06-17
        browseButton = (Button) findViewById(R.id.browse1);
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
     * Display the browsed image in photoImageView
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * Initializes the save button.   
     */
    void setUpSave() {
        createButton = (Button) findViewById(R.id.save);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMoodEvent();
            }
        });
    }

    /**
     * Initializes the cancel button.  
     */
    void setUpCancel() {
        cancelButton = (Button) findViewById(R.id.cancel1);
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

