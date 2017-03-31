package com.psychic_engine.cmput301w17t10.feelsappman;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageView;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.CreateMoodActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyProfileActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.robotium.solo.Solo;

/**
 * Created by jyuen1 on 3/8/17.
 * Commented by adong
 */

/**
 * CreateMoodActivity Test tests the CreateMoodActivity UI to ensure proper functionality. The first
 * few methods will be for initialization of the UI test
 * @see CreateMoodActivity
 */
public class CreateMoodActivityTest extends ActivityInstrumentationTestCase2<CreateMoodActivity> {

    private Solo solo;

    public CreateMoodActivityTest() {
        super(CreateMoodActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Multiple options are selected to make sure that the spinner returns the correct values. Cycles
     * through each option. As more emotions are added, more assertions will be required to test
     * those emotions. The activity should also be CreateMoodActivity
     * @see CreateMoodActivity
     * @see Mood
     **/
    public void testMoodSpinner() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
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
     * Similar to the mood spinner, the test cycles through the available options to ensure proper
     * functionality (ie. obtaining the right social setting). We will first need to ensure we are
     * at the proper activity (CreateMoodActivity)
     * @see CreateMoodActivity
     * @see SocialSetting
     */
    public void testSocialSettingSpinner() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
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
     * Test the functionality of the trigger EditText to ensure proper input.
     */
    public void testTriggerEditText() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
        // Enter trigger as Happy :)
        solo.enterText((EditText) solo.getView(R.id.trigger), "Happy :)");
        assertTrue("trigger edit text does not say \"Happy :)\"", solo.searchText("Happy :)"));
    }
    /*
    public void testLocationEditText() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
        // TODO part 5
    }
    */

    /**
     * Test to determine that the bitmap view is displaying the correct image that the participant
     * intends on viewing, after getting the image from the file.
     */
    public void testPhotoImageView() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
        ImageView photo = (ImageView) solo.getView(R.id.imageView);
        Bitmap photoBitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
        Drawable actualDrawable = getActivity().getResources().getDrawable(R.mipmap.ic_launcher);
        Bitmap actualBitmap = ((BitmapDrawable) actualDrawable).getBitmap();
        assertEquals(actualBitmap, photoBitmap);
    }

    /**
     * Tests the Browse button to ensure that the participant is sent to the file explorer
     */
    public void testBrowseButton() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
        solo.clickOnText("Browse");
        // TODO part 5
    }

    /**
     * Test the create button to ensure that when you click on the button, you will be sent to
     * the MyProfileActivity upon completion. If it does not create the mood properly, then either
     * the app will crash or you will not be sent to the MyProfileActivity.
     * @see MyProfileActivity
     */
    public void testCreateButton() {
        solo.assertCurrentActivity("Wrong Activity", CreateMoodActivity.class);
        // Enter test mood as Sad
        solo.pressSpinnerItem(0, 1);
        assertTrue("mood spinner item Sad is not selected", solo.isSpinnerTextSelected(0,"Sad"));
        // create test participant
        //ParticipantSingleton.getInstance().addParticipant("test");
        Participant self = ParticipantSingleton.getInstance().searchParticipant("test");
        ParticipantSingleton.getInstance().setSelfParticipant(self);
        // Press create button
        solo.clickOnText("Create");
        solo.assertCurrentActivity("Wrong Activity", MyProfileActivity.class);
    }

    // close activity
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
