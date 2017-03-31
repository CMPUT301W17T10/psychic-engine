package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.FollowRequestActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.FollowersActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.FollowingActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyFeedActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyProfileActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.robotium.solo.Solo;

import org.junit.Before;

/**
 * Created by adong on 2017-03-30.
 */

/**
 * Tests to determine the proper functionality in the MyFeedActivity.
 */
public class MyFeedActivityTest extends ActivityInstrumentationTestCase2<MyFeedActivity> {

    private Solo solo;
    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        Participant tester = new Participant("MyFeedActivityTester");
        if (ParticipantSingleton.getInstance().searchParticipant("MyFeedActivityTester") != tester) {
            ParticipantSingleton.getInstance().addParticipant(tester);
        }
        ParticipantSingleton.getInstance().setSelfParticipant(tester);
    }

    public MyFeedActivityTest() { super(MyFeedActivity.class);}


    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Test the spinners to ensure that the app directs the participant to the proper activity
     */
    public void test1_Spinner() {
        solo.assertCurrentActivity("Wrong Activity", MyFeedActivity.class);
        solo.pressSpinnerItem(0, 0);
        assertTrue("Feed not selected", solo.isSpinnerTextSelected(0, "My Feed"));
        solo.assertCurrentActivity("Wrong Activity", MyFeedActivity.class);

        solo.pressSpinnerItem(0, 1);
        assertTrue("Profile not selected", solo.isSpinnerTextSelected(0, "My Profile"));
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);

        solo.pressSpinnerItem(0, 1);
        assertTrue("Followers not selected", solo.isSpinnerTextSelected(0, "Followers"));
        solo.assertCurrentActivity("Wrong Activity", FollowersActivity.class);

        solo.pressSpinnerItem(0, 1);
        assertTrue("Following not selected", solo.isSpinnerTextSelected(0, "Following"));
        solo.assertCurrentActivity("Wrong Activity", FollowingActivity.class);

        solo.pressSpinnerItem(0, 1);
        assertTrue("Requests not selected", solo.isSpinnerTextSelected(0, "Follower Requests"));
        solo.assertCurrentActivity("Wrong Activity", FollowRequestActivity.class);
    }

    //TODO eventual adapter testing
}
