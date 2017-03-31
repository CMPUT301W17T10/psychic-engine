package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.LoginActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyFeedActivity;
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
    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * The first test determines the functionality of the sign up button. Tests to see that the
     * participant addition into the server is successful, unique participant names, and handling
     * empty fields. Since the elastic server actually registers these participants, it will only work
     * when the participants are not already in the server.
     */
    public void test1_SignUpButton() {

        // setup test
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        assertNotNull(ParticipantSingleton.getInstance());
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));

        // simple sign up without any errors
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant11");
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("TestParticipant11 has been added!"));
        solo.assertCurrentActivity("Not directed", MyFeedActivity.class);

        // test multiple additions
        solo.goBackToActivity("LoginActivity");
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant12");
        solo.sleep(1000);
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("TestParticipant12 has been added"));
        solo.assertCurrentActivity("Not directed", MyFeedActivity.class);

        // test unique name blocking
        solo.goBackToActivity("LoginActivity");
        solo.sleep(1000);
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("Unable to sign up as TestParticipant12"));

        // test empty text field handling
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.sleep(1000);
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("Invalid input"));
    }


    /**
     * The test determines whether or not the system will continue even when there is no
     * such participant in the system. Initially test that the button functionality properly rejects
     * entry into the app if the name has not been found. Next, test the functionality of allowing
     * entry into the app when there is a stored name.
     */
    public void test2_LoginButton() {

        // test empty input handling
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.clickOnText("Login");
        assertTrue(solo.waitForText("Invalid input"));

        // test non registered user
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant13");
        solo.clickOnText("Login");
        assertTrue(solo.waitForText("This participant does not exist, please sign up"));

        // test proper login
        solo.clearEditText((EditText) solo.getView(R.id.nameEditText));
        solo.enterText((EditText) solo.getView(R.id.nameEditText), "TestParticipant11");
        assertNotNull("Participant not found", ParticipantSingleton.getInstance().searchParticipant("TestParticipant11"));
        solo.clickOnText("Login");
        solo.assertCurrentActivity("Did not pass login activity", MyFeedActivity.class);
    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
