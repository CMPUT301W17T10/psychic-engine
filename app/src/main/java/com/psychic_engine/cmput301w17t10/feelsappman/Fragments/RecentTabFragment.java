package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 * Created by jordi on 2017-03-09.
 * Comments by adong on 3/28/2017.
 */
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.EditMoodActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.RecentMapActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.ViewMoodEventActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.DeleteMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import static android.graphics.Color.parseColor;

/**
 * RecentTabFragment contain the details of the most recent mood event that the participant has
 * edited or added. The background of the fragment will correspond to the mood state's corresponding
 * color. It is similar to the ViewMoodActivity, where you would be able to view any mood event,
 * even though it is not the most recent mood event for that participant.
 * @see ViewMoodEventActivity
 */
public class RecentTabFragment extends Fragment {

    private TextView date;
    private TextView viewMood;
    private TextView location;
    private ImageView imageView;
    private Button deleteButton;
    private Button editButton;
    private Participant participant;
    private MoodEvent moodEvent;
    private Button displayRecentMapButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recent, container, false);
        deleteButton = (Button)rootView.findViewById(R.id.deletebutton);
        editButton = (Button)rootView.findViewById(R.id.editbutton);
        date = (TextView) rootView.findViewById(R.id.date);
        viewMood = (TextView) rootView.findViewById(R.id.mood);
        location = (TextView) rootView.findViewById(R.id.location);
        imageView = (ImageView) rootView.findViewById(R.id.picture);
        displayRecentMapButton = (Button) rootView.findViewById(R.id.recentmap);
        participant = ParticipantSingleton.getInstance().getSelfParticipant();
        moodEvent = participant.getMostRecentMoodEvent();

        // set the deleteButton button to deleteMoodEvent mood events and editMoodEvent affected classes accordingly
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moodEvent != null) {
                    DeleteMoodController.deleteMoodEvent(moodEvent);
                    display();
                }
            }
        });

        // set the editButton button to send the user to the EditMoodActivity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMood();
            }
        });

        // set the imageview to send the user to the details of the mood event in ViewMoodActivity
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMood();
            }
        });

        displayRecentMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecentMapActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * This method will display the information about the most recent mood event of the participant.
     * Information about the mood event that was not given upon the edition or addition of the event
     * will not be displayed.
     */
    private void display() {
        moodEvent = participant.getMostRecentMoodEvent();

        if (moodEvent != null) {
            int color = parseColor(moodEvent.getMood().getColor().getBGColor());
            this.getView().setBackgroundColor(color);
            viewMood.setText(moodEvent.getMood().toString());
            date.setText(moodEvent.getDate().toString());
            if (moodEvent.getPicture() != null) {
                imageView.setImageBitmap(moodEvent.getPicture().getImage());
            }
            if (moodEvent.getLocation() != null)
                location.setText(moodEvent.getLocation().getLatitudeStr().concat(moodEvent.getLocation().getLongitudeStr()));
        } else {
            this.getView().setBackgroundColor(Color.BLACK);
            viewMood.setText("");
            date.setText("There's No Mood Event Yet! Why Don't you add one!");
            //location.setText("");
            imageView.setImageBitmap(null);
        }
    }

    /**
     * Launch the editButton mood event activity
     * passing it the uniqueID of the mood event to be edited as extras
     */
    private void editMood() {
        if (moodEvent != null) {
            Intent intent = new Intent(getActivity(), EditMoodActivity.class);
            intent.putExtra("moodEventId", moodEvent.getId());
            startActivity(intent);
        }
    }

    /**
     * Launch the view mood event activity
     * passing it the uniqueID of the mood event to be viewed as extras
     */
    private void viewMood() {
        if (moodEvent != null) {
            Intent intent = new Intent(getActivity(), ViewMoodEventActivity.class);
            intent.putExtra("moodEventId", moodEvent.getId());
            startActivity(intent);
        }
    }

    /**
     * Override onStart method to display the recent mood event upon the start of the fragment
     */
    @Override
    public void onStart() {
        super.onStart();
        // Refresh display
        display();
    }
}
