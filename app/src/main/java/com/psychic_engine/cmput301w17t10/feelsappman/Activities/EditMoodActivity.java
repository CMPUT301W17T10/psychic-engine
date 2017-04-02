package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.EditMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.EmptyMoodException;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.R;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

/**
 * Created by jyuen1 on 3/7/2017.
 * Location and photo by Pierre Lin on 3/28/2017
 */

/**
 * The EditMoodActivity pulls any current details about a certain mood event and presents them
 * to the participant. The participant will then be able to make adjustments to any part of the
 * mood event. Upon successful edit of the mood event, a new date is set at the current time. If
 * a location is enabled for the mood event, then their current location will also be recorded,
 * regardless of whether or not the mood event did not allow a location to be set.
 */
public class EditMoodActivity extends AppCompatActivity{
    private static int RESULT_LOAD_IMAGE = 1;
    private LocationManager lm;
    private LocationListener locationListener;
    private Spinner moodSpinner;
    private Spinner socialSettingSpinner;
    private EditText triggerEditText;
    private CheckBox locationCheckBox; // TODO: change type
    private EditText locationLat;
    private EditText locationLong;
    private Button browseButton;
    private ImageView photoImageView;
    private Button createButton;
    private Button cancelButton;

    private MoodEvent moodEvent;    // the moodEvent to be edited
    private String moodEventId;

    /**
     * Called on activity creation.  Initializes widgets and class variables as well as confirming
     * system permissions such as external storage and location.
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

        // request for permission by the participant if not permitted
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

        // set up events that happen when user clicks createMoodEvent button
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
     * Simple checker to determine if some EditText box is empty.
     * @param myeditText
     * @return
     */
    //Taken from http://stackoverflow.com/questions/24391809/android-check-if-edittext-is-empty
    //March 28, 2017
    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
    /**
     * Retrieves information set in widgets and calls EditMoodController to register changes.
     * @see EditMoodController
     */
    void saveMoodEvent() {
        //check if location checkbox is checked
        Boolean isChecked = locationCheckBox.isChecked();

        //check if editTexts for lat long are empty
        Boolean isLatEmpty = isEmpty(locationLat);
        Boolean isLongEmpty = isEmpty(locationLong);

        // get the mood from the mood spinner
        String moodString = moodSpinner.getSelectedItem().toString();

        // get the social setting from the social setting spinner
        String socialSettingString = socialSettingSpinner.getSelectedItem().toString();

        // get the trigger from the trigger edit text
        String trigger = triggerEditText.getText().toString();

        Photograph photo = moodEvent.getPicture();
        MoodLocation location = moodEvent.getLocation();

        boolean photoSizeUnder = TRUE;

        if (isChecked && (!isLatEmpty || !isLongEmpty)) {
            Toast.makeText(EditMoodActivity.this,
                    "Location input invalid",
                    Toast.LENGTH_LONG).show();
        }
        //use current location not checked, lat, long editTexts are not empty
        if (!isChecked && !isLatEmpty && !isLongEmpty) {
            double lat = Double.parseDouble(locationLat.getText().toString());
            double lon = Double.parseDouble(locationLong.getText().toString());
            location = new MoodLocation(new GeoPoint(lat, lon));
        }

        if (isChecked && isLatEmpty && isLongEmpty) {
            //TODO DO LOC STUFF, get current loc and make it location
            Location coords = new Location("GPS");
            coords = getCurrentLocation(coords);
            //set location as new MoodLocation as a Geopoint
            try {
                double lat = coords.getLatitude();
                double lon = coords.getLongitude();
                location = new MoodLocation(new GeoPoint(lat, lon));
            } catch (Exception e) {
                //pass
            }

        }

        try {
            Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
            photo = new Photograph(bitmap);
            photoSizeUnder = photo.getLimitSize();
        } catch (Exception e) {
            // pass
        }

        if (photoSizeUnder) {
            boolean thrown = false;
            try {
                EditMoodController.editMoodEvent(moodEvent, moodString, socialSettingString
                        , trigger, photo, location, getApplicationContext());
            } catch (EmptyMoodException e) {
                thrown = true;
                Toast.makeText(EditMoodActivity.this,
                        "Please specify a mood",
                        Toast.LENGTH_LONG).show();
            } catch (TriggerTooLongException e) {
                thrown = true;
                Toast.makeText(EditMoodActivity.this,
                        "Trigger must be 3 words and under 20 chars!",
                        Toast.LENGTH_LONG).show();
            }

            if (!thrown) {
                Intent intent = new Intent(EditMoodActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }

        } else {
            Toast.makeText(EditMoodActivity.this,
                    "Photo size is too large! (Max 65536 bytes)",
                    Toast.LENGTH_LONG).show();
        }

    }

    public Location getCurrentLocation(Location coords) {
        //Taken from http://stackoverflow.com/questions/17584374/check-if-gps-and-or-mobile-network-location-is-enabled
        //March 27, 2017
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        //Create new Location object using provider
        Boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //GPS service gets FINE location
        //Network provider gets COARSE location
        if (gps) {
            //Ignore warnings, permissions checked when activity starts
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            if (lm != null) {
                coords = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

        }
        if (!gps && network) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            if (lm!=null) {
                coords = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
        //no GPS or network provider
        if (!gps && !network) {
            Toast.makeText(EditMoodActivity.this,
                    "You are not connected to GPS or a network provider",
                    Toast.LENGTH_LONG).show();
        }
        return coords;
    }

    /**
     * Initializes and adds categories to spinners, as well as set up adapters for the spinners.
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
        if (moodEvent.getLocation() != null) {
            ((TextView) findViewById(R.id.locationLat)).setHint(moodEvent.getLocation().getLatitudeStr());
            ((TextView) findViewById(R.id.locationLong)).setHint(moodEvent.getLocation().getLongitudeStr());
        }
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
        locationCheckBox = (CheckBox) findViewById(R.id.editToCurrentLocation);
        locationLat = (EditText) findViewById(R.id.locationLat);
        locationLong = (EditText) findViewById(R.id.locationLong);
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

    /**
     * Attempt to save the instance when the activity pauses
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileManager.saveInFile(this);
    }

    /**
     * Attempt to save the instance when the activity stops running
     */
    @Override
    public void onStop() {
        super.onStop();
        FileManager.saveInFile(this);
    }

}

