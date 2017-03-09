package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by jyuen1 on 3/8/17.
 */

public class CreateMoodActivityTest extends ActivityInstrumentationTestCase2<CreateMoodActivity> {

    private Solo solo;

    public CreateMoodActivityTest() {
        super(com.psychic_engine.cmput301w17t10.feelsappman.CreateMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testCreateMood() {

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

        // Click on create button
        solo.clickOnButton("Create");
        System.out.println("--------------- Create should add above");
        Participant selfParticipant = ParticipantSingleton.getInstance().getSelfParticipant();

        // Test if size of moodList is 1
        ArrayList<MoodEvent> moodEventList = selfParticipant.getMoodList();
        assertTrue("size of mood list is not 1", moodEventList.size() == 1);

        // Test if moodEvent contains the correct data
        MoodEvent moodEvent = moodEventList.get(0);
        assertTrue("mood event does not contain correct mood state", moodEvent.getMood().getMood() == MoodState.HAPPY);
        assertTrue("mood event does not contain correct mood color",moodEvent.getMood().getColor() == MoodColor.GREEN);
        // assertTrue("mood event does not contain correct mood icon", moodEvent.getMood().getIcon() == "happyicon.png"); TODO when working icon
        assertTrue("mood event does not contain correct social setting",moodEvent.getSocialSetting() == SocialSetting.ONEOTHER);
        assertTrue("mood event does not contain correct trigger",moodEvent.getTrigger() == "Happy :)");
        assertTrue("mood event does not contain correct location", moodEvent.getLocation() == null);
        assertTrue("mood event does not contain correct picture",moodEvent.getPicture() == null);
    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
