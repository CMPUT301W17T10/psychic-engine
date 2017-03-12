package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by jyuen1 on 3/8/17.
 * Commented by Alex Dong on 3/12/2017
 */

/**
 * UI Testing for the CreateMoodActivity,
 */
public class CreateMoodActivityTest extends ActivityInstrumentationTestCase2<CreateMoodActivity> {

    private Solo solo;
    private ParticipantSingleton instance;

    //TODO: ParticipantSingleton currently does not call saveInFile() except for the login activity.
    //TODO: Resets everything because instance is not saved. When we enter method, need to change.
    /**
     * Initialize the instance if it does not exist. If instance is initialized prior, then use
     * that instance. Due to the nature of the singleton, need to check if the count is 0, then
     * we would need to manually enter some participant in there and set the new moods into that
     * participant.
     *
     */
    public CreateMoodActivityTest() {
        super(com.psychic_engine.cmput301w17t10.feelsappman.CreateMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        instance = ParticipantSingleton.getInstance();
        if (instance.getParticipantCount() == 0) {
            instance.addParticipant("alex");
            instance.setSelfParticipant(instance.getParticipantList().get(0));
        }
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void test1_CreateMood() throws InterruptedException {

        CreateMoodActivity activity = (CreateMoodActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);

        // Select mood spinner item Sad
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Sad is not selected", solo.isSpinnerTextSelected(0,"Sad"));

        // Select mood spinner item Happy
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Happy is not selected", solo.isSpinnerTextSelected(0, "Happy"));

        // Select social setting spinner item Alone
        solo.pressSpinnerItem(1, 1);
        assertTrue("social setting spinner item Alone is not selected", solo.isSpinnerTextSelected(1, "Alone"));

        // Select social setting spinner item One Other
        solo.pressSpinnerItem(1, 1);
        assertTrue("social setting spinner item One Other is not selected", solo.isSpinnerTextSelected(1, "One Other"));

        // Enter trigger as Happy :)
        solo.enterText((EditText) solo.getView(R.id.trigger), "Happy :)");
        assertTrue("trigger edit text does not say \"Happy :)\"", solo.searchText("Happy :)"));

        // TODO Create tests for location and photograph when we know how they work
        // TODO: Jumps to getMoodList and the nadding Mood Event
        // Click on create button
        Log.d("SelfParticipant", instance.getSelfParticipant().getLogin());
        solo.clickOnText("Create");
        // Test if moodEvent contains the correct data
        Log.d("Count of mood", "Size: "+String.valueOf(instance.getSelfParticipant().getMoodList().size()));
        MoodEvent getEvent = instance.getSelfParticipant().getMoodList().get(0);
        assertTrue("mood event does not contain correct mood state", getEvent.getMood().getMood() == MoodState.HAPPY);
        /*
        assertTrue("mood event does not contain correct mood color",moodEvent.getMood().getColor() == MoodColor.GREEN);
        // assertTrue("mood event does not contain correct mood icon", moodEvent.getMood().getIcon() == "happyicon.png"); TODO when working icon
        assertTrue("mood event does not contain correct social setting",moodEvent.getSocialSetting() == SocialSetting.ONEOTHER);
        assertTrue("mood event does not contain correct trigger",moodEvent.getTrigger() == "Happy :)");
        assertTrue("mood event does not contain correct location", moodEvent.getLocation() == null);
        assertTrue("mood event does not contain correct picture",moodEvent.getPicture() == null);
        */

    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
