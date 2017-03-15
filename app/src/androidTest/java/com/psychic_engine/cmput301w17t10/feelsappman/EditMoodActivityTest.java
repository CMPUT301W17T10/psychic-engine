package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageView;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.EditMoodActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.SelfNewsFeedActvity;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.robotium.solo.Solo;

/**
 * Created by jyuen1 on 3/12/2017.
 */

/**
 * EditMoodActivity Test tests the EditMoodActivity UI to ensure proper functionality. The first
 * few methods will be for initialization of the UI test.
 * @see EditMoodActivity
 */
public class EditMoodActivityTest  extends ActivityInstrumentationTestCase2<EditMoodActivity> {
    private Solo solo;

    public EditMoodActivityTest() {
        super(EditMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Mood spinner will determine which mood has been selected. The test ensures that the mood
     * is selected correctly corresponding to what the spinner has selected.
     * @see Mood
     */
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

    /**
     * Test the social setting selector in the spinner. The test will ensure that the values will
     * correspond to whatever the spinner has selected.
     * @see SocialSetting
     */
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

    /**
     * Tests the trigger EditText to ensure that the input that the participant has entered
     * is correct. This test does not test whether or not the text is short enough (3 words and <
     * 20 characters long)
     */
    public void testTriggerEditText() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // Enter trigger as Happy :)
        solo.enterText((EditText) solo.getView(R.id.trigger), "Happy :)");
        assertTrue("trigger edit text does not say \"Happy :)\"", solo.searchText("Happy :)"));
    }

    /*
    public void testLocationEditText() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        // TODO part 5
    }
    */

    /**
     * Tests the image view that displays the picture that the participant decides to upload. The
     * bitmap is compared to the other bitmap through the bytes.
     * @see Bitmap
     * @see Photograph
     */
    public void testPhotoImageView() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        ImageView photo = (ImageView) solo.getView(R.id.imageView);
        Bitmap photoBitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
        Drawable actualDrawable = getActivity().getResources().getDrawable(R.mipmap.ic_launcher);
        Bitmap actualBitmap = ((BitmapDrawable) actualDrawable).getBitmap();
        assertEquals(actualBitmap, photoBitmap);
    }

    /**
     * Tests the browse button to ensure that the the participant is not stuck in the activity.
     */
    public void testBrowseButton() {
        solo.assertCurrentActivity("Wrong Activity", EditMoodActivity.class);
        solo.clickOnText("Browse");
        // TODO part 5
    }

    /**
     * Tests the save button to ensure that the mood events that the participant has requested
     * to edit has been saved. The participant will also be sent back to their profile upon
     * completion.
     */
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
