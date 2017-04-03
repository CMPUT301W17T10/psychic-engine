package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

/**
 * Created by jyuen1 on 3/12/2017.
 * Comments by adong on 3/28/2017.
 */

/**
 * ViewMoodEventActivity is called upon a clicked mood event on some list view. Inflates all the
 * information of the mood event onto the participants screen. This includes the background colour
 * which represents the mood which corresponds to their mood states. Triggers or pictures as well
 * as social setting is presented if available. An emoji corresponding to its mood state will also
 * be shown.
 * @see MoodEvent
 */
public class ViewMoodEventActivity extends AppCompatActivity{
    private TextView name;
    private TextView dateTime;
    private ImageView photo;
    private TextView location;
    private ImageView icon;
    private TextView trigger;
    private TextView socialIcon;    // Temporary for now to meet requirements until we have icon for social setting
    private ImageButton returnButton;
    private Participant participant;
    private MoodEvent moodEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_event);

        name = (TextView) findViewById(R.id.me_name);
        dateTime = (TextView) findViewById(R.id.me_date_time);
        photo = (ImageView) findViewById(R.id.me_photo);
        location = (TextView) findViewById(R.id.me_location);
        icon = (ImageView) findViewById(R.id.me_icon);
        trigger = (TextView) findViewById(R.id.me_trigger);
        socialIcon = (TextView) findViewById(R.id.me_social);
        returnButton = (ImageButton) findViewById(R.id.me_return);

        moodEvent = (MoodEvent) getIntent().getExtras().getSerializable("moodEvent");

        if (moodEvent != null)
            display();

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * The display method that shows all of the information that is contained in the mood event.
     * Optional information is displayed if they were given upon creation/edit. Otherwise, the mood
     * state as well as its corresponding colour will be displayed as well as a small emoji to also
     * represent the mood.
     */
    private void display() {
        // set background color
        int color = parseColor(moodEvent.getMood().getColor().getBGColor());
        getWindow().getDecorView().setBackgroundColor(color);

        // set participant's name
        name.setText(moodEvent.getMoodOwner());

        // set date
        dateTime.setText(moodEvent.getDate().toString());

        // set icon
        String iconName = moodEvent.getMood().getIconName();
        int drawableResourceId = this.getResources().getIdentifier(iconName, "drawable", getPackageName());
        icon.setImageResource(drawableResourceId);

        // set picture
        if (moodEvent.getPicture() != null)
            photo.setImageBitmap(moodEvent.getPicture().getImage());
        else
            photo.setImageResource(drawableResourceId);

        // set location
        if (moodEvent.getLocation() != null)
            location.setText(moodEvent.getLocation().getLatitudeStr().concat(moodEvent.getLocation().getLongitudeStr()));

        // set trigger
        trigger.setText(moodEvent.getTrigger());
        if (moodEvent.getSocialSetting() != null)
            socialIcon.setText(moodEvent.getSocialSetting().toString());
    }

    /**
     * Attempt to save the instance when the activity pause
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
