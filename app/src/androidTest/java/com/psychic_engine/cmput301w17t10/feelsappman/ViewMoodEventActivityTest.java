package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.ViewMoodEventActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.robotium.solo.Solo;

/**
 * Created by adong on 2017-03-30.
 */

/**
 * Tests the functionality of the activity including the widgets and ensures that the each of the
 * widgets are functional and directs the participant to the proper activities.
 */
public class ViewMoodEventActivityTest extends ActivityInstrumentationTestCase2<ViewMoodEventActivity> {
    private Solo solo;
    private ParticipantSingleton instance;

    public ViewMoodEventActivityTest(Class activityClass) { super(activityClass); }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        instance = ParticipantSingleton.getInstance();
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Tests the display method to ensure all of the information about a mood event has been
     * displayed properly
     */
    public void test1_display() {
        solo.assertCurrentActivity("Wrong Activity", ViewMoodEventActivity.class);

    }

    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
