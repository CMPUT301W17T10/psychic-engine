package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageView;

import com.robotium.solo.Solo;

/**
 * Created by jyuen1 on 3/12/2017.
 */

public class EditMoodActivityTest  extends ActivityInstrumentationTestCase2<EditMoodActivity> {
    private Solo solo;

    public EditMoodActivityTest() {
        super(com.psychic_engine.cmput301w17t10.feelsappman.EditMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testMoodSpinner() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // Select mood spinner item Sad
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Sad is not selected", solo.isSpinnerTextSelected(0,"Sad"));
        // Select mood spinner item Happy
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Happy is not selected", solo.isSpinnerTextSelected(0, "Happy"));
        // Select mood spinner item Shame
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Shame is not selected", solo.isSpinnerTextSelected(0, "Shame"));
        // Select mood spinner item Fear
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Fear is not selected", solo.isSpinnerTextSelected(0, "Fear"));
        // Select mood spinner item Anger
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Anger is not selected", solo.isSpinnerTextSelected(0, "Anger"));
        // Select mood spinner item Surprised
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Surprised is not selected", solo.isSpinnerTextSelected(0, "Surprised"));
        // Select mood spinner item Disgust
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Disgust is not selected", solo.isSpinnerTextSelected(0, "Disgust"));
        // Select mood spinner item Confused
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Confused is not selected", solo.isSpinnerTextSelected(0, "Confused"));
    }

    public void testSocialSettingSpinner() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // Select social setting spinner item Alone
        solo.pressSpinnerItem(1, 1);
        assertTrue("social setting spinner item Alone is not selected", solo.isSpinnerTextSelected(1, "Alone"));
        // Select social setting spinner item One Other
        solo.pressSpinnerItem(1, 1);
        assertTrue("social setting spinner item One Other is not selected", solo.isSpinnerTextSelected(1, "One Other"));
        // Select social setting spinner item Two To Several
        solo.pressSpinnerItem(1, 1);
        assertTrue("social setting spinner item Two To Several is not selected", solo.isSpinnerTextSelected(1, "Two To Several"));
        // Select social setting spinner item Crowd
        solo.pressSpinnerItem(1, 1);
        assertTrue("social setting spinner item Crowd is not selected", solo.isSpinnerTextSelected(1, "Crowd"));
    }

    public void testTriggerEditText() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // Enter trigger as Happy :)
        solo.enterText((EditText) solo.getView(R.id.trigger), "Happy :)");
        assertTrue("trigger edit text does not say \"Happy :)\"", solo.searchText("Happy :)"));
    }

    public void testLocationEditText() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // TODO part 5
    }

    public void testPhotoImageView() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        ImageView photo = (ImageView) solo.getView(R.id.imageView);
        Bitmap photoBitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
        Drawable actualDrawable = getActivity().getResources().getDrawable(R.mipmap.ic_launcher);
        Bitmap actualBitmap = ((BitmapDrawable) actualDrawable).getBitmap();
        assertEquals(actualBitmap, photoBitmap);
    }

    public void testBrowseButton() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        solo.clickOnText("Browse");
        // TODO part 5
    }

    public void testSaveButton() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // Enter test mood as Sad
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Sad is not selected", solo.isSpinnerTextSelected(0,"Sad"));
        // create test participant
        ParticipantSingleton.getInstance().addParticipant("test");
        Participant self = ParticipantSingleton.getInstance().searchParticipant("test");
        ParticipantSingleton.getInstance().setSelfParticipant(self);
        // Press create button
        solo.clickOnText("Save");
        solo.assertCurrentActivity("Wrong Activity", SelfNewsFeedActvity.class);
    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
