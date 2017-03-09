package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
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
        Log.d("Before Clear" , String.valueOf(ParticipantSingleton.getInstance().getParticipantCount()));
        ParticipantSingleton.getInstance().getParticipantList().clear();
        Log.d("After Clear" , String.valueOf(ParticipantSingleton.getInstance().getParticipantCount()));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant1");
        // TODO getParticipantCount() does not return 0 even after clearing
        assertTrue(ParticipantSingleton.getInstance().getParticipantCount() == 0);

        solo.clickOnText("Sign Up");
        // Test if Sign up added a user into the GSON
        assertTrue(ParticipantSingleton.getInstance().getParticipantCount() == 1);
        // ParticipantTestList connects to the participantlist in activity??
        Participant firstParticipant = ParticipantSingleton.getInstance().getParticipantList().get(0);
        assertEquals("TestParticipant1", firstParticipant.getLogin());

        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant1");
        // Test if activity added a duplicate username or not (unique name) US 03.01.01
        assertTrue(ParticipantSingleton.getInstance().getParticipantCount() == 1);
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant2");
        solo.clickOnText("Sign Up");
        assertTrue(ParticipantSingleton.getInstance().getParticipantCount() == 2);
        solo.clickOnText("Sign Up");
        assertTrue(ParticipantSingleton.getInstance().getParticipantCount() == 2);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    public void testLogin() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        ParticipantSingleton.getInstance().getParticipantList().clear();
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.assertCurrentActivity("SIGN IN PASS WITHOUT CHECK", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant3");
        solo.clickOnText("Login");
        solo.assertCurrentActivity("SIGN IN PASS WITHOUT CHECK", LoginActivity.class);

        // Current activity should be LoginActivity
        // If it is SelfNewsFeed Activity, then sign in pass without checking if name exists

        solo.clickOnText("Sign Up");
        solo.clickOnText("Login");
        solo.assertCurrentActivity("SIGN IN PASS WITHOUT CHECK", LoginActivity.class);
        solo.clickOnButton("Sign up");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("SIGN IN DID NOT PASS TO NEXT ACTIVITY", SelfNewsFeedActvity.class);



    }
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
