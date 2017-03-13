package com.psychic_engine.cmput301w17t10.feelsappman;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jyuen1 on 3/12/2017.
 */

/**
 * This is an activity/view class that displays information about a mood.
 */
public class ViewMoodEventActivity extends AppCompatActivity{
    private TextView name;
    private TextView dateTime;
    private ImageView photo;
    private TextView location;
    private ImageView icon;
    private TextView trigger;
    private ImageView socialIcon;

    ParticipantSingleton instance;
    int moodEventPosition;
    MoodEvent moodEvent;

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
        socialIcon = (ImageView) findViewById(R.id.me_social);


        moodEventPosition = getIntent().getExtras().getInt("moodEventPosition");
        instance = ParticipantSingleton.getInstance();
        Participant self = instance.getSelfParticipant();

        MoodEvent moodEvent = null;
        try {
            ArrayList<MoodEvent> moodEventList = self.getMoodList();
            moodEvent = moodEventList.get(moodEventPosition);    // TODO: get appropriate mood later, right now gets the most recent image
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        //TODO - tomorrow ill make it display color in text (just to satisfy reqs for now), icon in text, and other stuff
        name.setText(self.getLogin());
        dateTime.setText(moodEvent.getDate().toString());
        //photo
        //location
        //icon
        trigger.setText(moodEvent.getTrigger());
        //socialIcon

    }
}
