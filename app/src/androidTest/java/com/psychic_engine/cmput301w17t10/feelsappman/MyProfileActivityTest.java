package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyFeedActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyProfileActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.robotium.solo.Solo;

/**
 * Created by adong on 2017-03-10.
 */

public class MyProfileActivityTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    private Solo solo;
    ParticipantSingleton instance;

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        Participant tester = new Participant("MyProfileActivityTester");
        if (ParticipantSingleton.getInstance().searchParticipant("MyProfileActivityTester") != tester) {
            ParticipantSingleton.getInstance().addParticipant(tester);
        }
        ParticipantSingleton.getInstance().setSelfParticipant(tester);

        instance = ParticipantSingleton.getInstance();
    }

    public MyProfileActivityTest() { super(MyProfileActivity.class);}

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testTabs() {
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
        TabLayout tabs = (TabLayout) solo.getView(android.R.id.tabs);
        View tab;
        tab = tabs.getChildAt(0);
        solo.clickOnView(tab);
        tab = tabs.getChildAt(2);
        solo.clickOnView(tab);
        tab = tabs.getChildAt(2);
        solo.clickOnView(tab);
    }

    // close activity
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}

         /*~********************************************************
        /                    TEST DATA CHARTS                     /
        *********************************************************/


//        participant.getMoodList().clear();
//
//        Mood testSadMood = new Mood(MoodState.SAD);
//        MoodEvent testSad1 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad2 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad3 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad4 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad5 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad6 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad7 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad8 = new MoodEvent(testSadMood, null, "", null, null);
//
//        testSad1.setDate(parseDate("2017-03-25"));
//        testSad2.setDate(parseDate("2017-03-25"));
//        testSad3.setDate(parseDate("2017-04-01"));
//        testSad4.setDate(parseDate("2017-05-01"));
//        testSad5.setDate(parseDate("2017-05-15"));
//        testSad6.setDate(parseDate("2018-03-25"));
//        testSad7.setDate(parseDate("2019-03-25"));
//        testSad8.setDate(parseDate("2020-03-25"));
//
//        participant.addMoodEvent(testSad1);
//        participant.addMoodEvent(testSad2);
//        participant.addMoodEvent(testSad3);
//        participant.addMoodEvent(testSad4);
//        participant.addMoodEvent(testSad5);
//        participant.addMoodEvent(testSad6);
//        participant.addMoodEvent(testSad7);
//        participant.addMoodEvent(testSad8);
//
//
//
//
//
//        Mood testHappyMood = new Mood(MoodState.HAPPY);
//        MoodEvent testHappy1 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy2 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy3 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy4 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy5 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy6 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy7 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy8 = new MoodEvent(testHappyMood, null, "", null, null);
//
//        testHappy1.setDate(parseDate("2017-03-27"));
//        testHappy2.setDate(parseDate("2017-03-27"));
//        testHappy3.setDate(parseDate("2017-03-27"));
//        testHappy4.setDate(parseDate("2017-04-25"));
//        testHappy5.setDate(parseDate("2017-04-25"));
//        testHappy6.setDate(parseDate("2017-04-28"));
//        testHappy7.setDate(parseDate("2019-03-23"));
//        testHappy8.setDate(parseDate("2020-03-25"));
//
//        participant.addMoodEvent(testHappy1);
//        participant.addMoodEvent(testHappy2);
//        participant.addMoodEvent(testHappy3);
//        participant.addMoodEvent(testHappy4);
//        participant.addMoodEvent(testHappy5);
//        participant.addMoodEvent(testHappy6);
//        participant.addMoodEvent(testHappy7);
//        participant.addMoodEvent(testHappy8);
//
//
//
//        Mood testShameMood = new Mood(MoodState.SHAME);
//
//        MoodEvent testShame1 = new MoodEvent(testShameMood, null, "", null, null);
//
//        testShame1.setDate(parseDate("2017-03-29"));
//
//        participant.addMoodEvent(testShame1);


        /*~*********************************************************
         *                         TEST DATA                       /
         *********************************************************/