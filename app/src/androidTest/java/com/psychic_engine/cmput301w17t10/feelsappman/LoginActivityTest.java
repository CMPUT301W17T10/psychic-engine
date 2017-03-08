package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by adong on 2017-03-07.
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginActivityTest() {
        super(com.psychic_engine.cmput301w17t10.feelsappman.LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testSignup() {
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant1");
        solo.clickOnButton("SIGN UP");

        // ParticipantTestList connects to the participantlist in activity??
        ArrayList<Participant> participantTestList = activity.getParticipantList();
        Participant firstParticipant = participantTestList.get(0);
        assertEquals("TestParticipant1", firstParticipant.getLogin());
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant1");
        participantTestList = activity.getParticipantList();

        // Test if activity added a duplicate username or not (unique name) US 03.01.01
        assertTrue(participantTestList.size() == 1);
    }

    public void testSignin() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));

        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant2");
        solo.clickOnButton("LOGIN");

        // Current activity should be LoginActivity
        // If it is NewsFeed Activity, then sign in pass without checking if name exists
        solo.assertCurrentActivity("SIGN IN PASS WITHOUT CHECK", LoginActivity.class);
    }
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
