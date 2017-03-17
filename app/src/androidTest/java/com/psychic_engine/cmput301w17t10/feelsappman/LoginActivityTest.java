package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.LoginActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.SelfNewsFeedActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.robotium.solo.Solo;

/**
 * Created by adong on 2017-03-07.
 */

/**
 * LoginActivityTest is the UI testing using Robotium to test the functionality of LoginActivity
 * Initial setup is done on the activity and the participant list should be cleared out before as
 * the singleton will have stored previous entries if this is not the first run of test cases.
 * This will cause the system to attempt to sign up for names that are already stored.
 * @see LoginActivity
 *
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    ParticipantSingleton instance;
    public LoginActivityTest() {
        super(LoginActivity.class);

        if (ParticipantSingleton.getInstance().getParticipantList() != null) {
            ParticipantSingleton.getInstance().getParticipantList().clear();
        }
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        instance = ParticipantSingleton.getInstance();
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * The first test determines the functionality of simply signing up with a valid, free name.
     * System will say that the username has been added and thus stored
     * Input: TestParticipant11 into the EditText and then presses "Sign up"
     */
    // Note: Singleton may store its participants that has been there before, so the count may
    // not be the same as before
    // TODO: Worry about the storage of the participants until elastic search can be done
    public void test1_SignupAvailableName() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        assertNotNull(ParticipantSingleton.getInstance());
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant11");
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("TestParticipant11 has been added!"));
    }

    /**
     * The second test determines the functionality of signing up another participant with its
     * own valid and free name.
     * Input: TestParticipant12 into the EditText and then presses "Sign Up"
     */
    public void test2_SignupAnotherName() {
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant12");
        // Test if activity added a duplicate username or not (unique name) US 03.01.01
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("TestParticipant12 has been added"));
    }

    /**
     * The third test determines the functionality of attempting to add a participant into the
     * system when this participant has the same name as another participant in the system.
     * Input: TestParticipant12 into the EditText and then pressing "Sign Up"
     * System should block the request and prompt the app user that the name has already been
     * taken
     */
    public void test3_SignupNameTaken() {
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant12");
        // Test if activity added a duplicate username or not (unique name) US 03.01.01
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("The username is already taken"));
    }

    /**
     * The fourth test determines the functionality of attempting to signup with an invalid name.
     * The invalid name is subjective for the time being (special characters/symbols etc.)
     * Input: An empty field in the EditText and attempting to sign up.
     * System should block the request and prompt the app user that it is invalid input and to
     * try again.
     */
    public void test4_SignupNameInvalid() {
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        // Test if activity added a duplicate username or not (unique name) US 03.01.01
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("Input invalid, please try again"));
    }

    /**
     * The fifth test determines whether or not the system will continue even when there is no
     * such participant in the system.
     * Input: TestParticipant13 into EditText (not yet signed up)
     * System will prompt the app user that the participant does not exist, and to sign up instead
     */
    public void test5_LoginWithoutStoredName() {
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant13");
        solo.clickOnText("Login");
        assertTrue(solo.waitForText("This participant does not exist, please sign up"));
    }

    /**
     * The sixth test determines the ideal scenario where there is a valid name in the system and
     * the participant attempts to login to the app
     * Input: TestParticipant11 (already signed up in the system)
     * The system will bring the user to the SelfNewsFeedActivity where their personal profile is
     * shown.
     * @see SelfNewsFeedActivity
     */
    public void test6_LoginWithStoredName() {
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant11");
        assertNotNull("Participant not found", ParticipantSingleton.getInstance().searchParticipant("TestParticipant11"));
        for (Participant participant : instance.getParticipantList()) {
            Log.d("Participants", participant.getLogin());
        }
        solo.clickOnText("Login");
        solo.searchText("Welcome TestParticipant11");
        solo.assertCurrentActivity("Did not pass login activity", SelfNewsFeedActivity.class);
    }

    /**
     * Fin.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
