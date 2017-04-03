package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.RecentMapActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.robotium.solo.Solo;

import org.junit.Before;

/**
 * Created by Pierre Lin on 4/2/2017.
 */

public class RecentMapActivityTest extends ActivityInstrumentationTestCase2<RecentMapActivity> {

    private Solo solo;

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
//        Participant tester = new Participant("MyRecentMapActivityTester");
//        if (ParticipantSingleton.getInstance().searchParticipant("MyRecentMapActivityTester") != tester) {
//            ParticipantSingleton.getInstance().addParticipant(tester);
//        }
//        ParticipantSingleton.getInstance().setSelfParticipant(tester);
    }

    public RecentMapActivityTest() {
        super(RecentMapActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();

    }
}
