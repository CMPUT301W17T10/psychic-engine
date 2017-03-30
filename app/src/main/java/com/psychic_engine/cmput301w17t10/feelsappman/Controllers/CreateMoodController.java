package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.CreateMoodActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodLocation;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;

/**
 * Created by jyuen1 on 3/7/17.
 * Comments by adong on 3/28/2017
 */

/**
 * CreateMoodController handles the creation of some mood event that a participant would like to
 * make. This is where depending on what specifications were chosen in the CreateMoodActivity, the
 * mood event will be created as such. The controller action is called upon the click of "Create"
 * in the activity. This is where limitations are tested such as the limit of reason's length
 * and amount of words (less than 20 letters and less than 3 words). The mood event will be created
 * under the singleton which is the participant using the app currently as well as in the elastic
 * server.
 * @see CreateMoodActivity
 * @see ElasticParticipantController
 * @see ElasticMoodController
 */
public class CreateMoodController {

    public static int updateMoodEventList(String moodString, String socialSettingString, String trigger, Photograph photo, MoodLocation location) {

        Mood mood;
        SocialSetting socialSetting;

        // determine mood state by the spinner's sent string
        switch(moodString) {
            case "Sad":
                mood = new Mood(MoodState.SAD);
                break;
            case "Happy":
                mood = new Mood(MoodState.HAPPY);
                break;
            case "Shame":
                mood = new Mood(MoodState.SHAME);
                break;
            case "Fear":
                mood = new Mood(MoodState.FEAR);
                break;
            case "Anger":
                mood = new Mood(MoodState.ANGER);
                break;
            case "Surprised":
                mood = new Mood(MoodState.SURPRISED);
                break;
            case "Disgust":
                mood = new Mood(MoodState.DISGUST);
                break;
            case "Confused":
                mood = new Mood(MoodState.CONFUSED);
                break;
            default:
                return -1;
        }

        // determine the social setting depending on what was chosen in the spinner
        switch (socialSettingString) {
            case "Alone":
                socialSetting = SocialSetting.ALONE;
                break;
            case "One Other":
                socialSetting = SocialSetting.ONEOTHER;
                break;
            case "Two To Several":
                socialSetting = SocialSetting.TWOTOSEVERAL;
                break;
            case "Crowd":
                socialSetting = SocialSetting.CROWD;
                break;
            default:
                socialSetting = null;
        }

        // test the reason to see if its within the length and word count
        int numWords = trigger.trim().split("\\s+").length;
        if (numWords > 3)
            return -2;

        // initialize the mood event with the given specifications
        MoodEvent moodEvent = new MoodEvent(mood, socialSetting, trigger, photo, location);

        // add to participant locally
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        Log.i("Add", "Adding to the self participant "+ ParticipantSingleton.getInstance().getSelfParticipant().getLogin());
        participant.addMoodEvent(moodEvent);

        // add to the elastic server
        ElasticMoodController.AddMoodEventTask addMoodEventTask = new ElasticMoodController
                .AddMoodEventTask();
        addMoodEventTask.execute(moodEvent);

        // update most recent mood event to be this mood event
        participant.setMostRecentMoodEvent(moodEvent);

        // update the participant in the elastic server to show the new/recent mood event
        Log.i("Update", "Updating participant in CreateMoodController");
        ElasticParticipantController.UpdateParticipantTask updateParticipantTask = new ElasticParticipantController.UpdateParticipantTask();
        updateParticipantTask.execute(participant);

        return 0;
    }
}
