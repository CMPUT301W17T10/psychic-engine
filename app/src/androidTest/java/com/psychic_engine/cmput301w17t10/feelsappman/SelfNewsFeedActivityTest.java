package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by adong on 2017-03-10.
 */

public class SelfNewsFeedActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    ParticipantSingleton instance;

    public SelfNewsFeedActivityTest(Class activityClass) {
        super(activityClass);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        instance = ParticipantSingleton.getInstance();
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
}
