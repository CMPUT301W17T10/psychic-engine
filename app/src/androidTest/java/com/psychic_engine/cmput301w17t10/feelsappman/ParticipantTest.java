package com.psychic_engine.cmput301w17t10.feelsappman;

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;

import junit.framework.TestCase;

/**
 * Created by jspun on 2/27/17.
 */
/*
public class ParticipantTest extends TestCase{

    public void testGetLogin() {
        Participant a = new Participant("alex");
        assertEquals(a.getLogin(), "alex");
    }
    public void testGetMoodList(){
        Participant a = new Participant("first");
        Mood mood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, null, null, null ,null);
        a.moodEvents.add(moodEvent);
        assertEquals(a.moodEvents,a.getMoodList());
    }

    public void testGetFollowers(){
        Participant a = new Participant("first");
        Participant b = new Participant("second");
        a.followers.add(b);
        assertEquals(a.followers,a.getFollowers());
    }

    public void testGetFollowing(){
        Participant a = new Participant("first");
        Participant b = new Participant("second");
        a.following.add(b);
        assertEquals(a.following,a.getFollowing());
    }

    public void testGetPendingRequest(){
        Participant a = new Participant("first");
        Participant b = new Participant("second");
        a.pendingRequests.add(b);
        assertEquals(a.pendingRequests,a.getPendingRequests());
    }

    public void testAddMoodEvent() {
        Participant a = new Participant("alex");
        Mood mood = new Mood(MoodState.HAPPY);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        a.addMoodEvent(moodEvent);
        assertTrue(a.moodEvents.contains(moodEvent));
    }

    public void testSetMoodEvent() {
        Participant a = new Participant("alex");
        Mood mood = new Mood(MoodState.HAPPY);
        Mood replaceMood = new Mood(MoodState.SAD);
        MoodEvent moodEvent = new MoodEvent(mood, SocialSetting.ALONE, "301", null, null);
        MoodEvent replaceMoodEvent = new MoodEvent(replaceMood, SocialSetting.CROWD, "Life", null, null);
        a.moodEvents.add(moodEvent);
        assertTrue(a.moodEvents.contains(moodEvent));
        a.setMoodEvent(0, replaceMoodEvent);
        assertTrue(a.moodEvents.contains(replaceMoodEvent));
    }
    // Not really required for the Participant model

    public void testSetMoodEvent() {
        Participant a = new Participant("first");
        Mood mood = new Mood(MoodState.ANGER);
        MoodEvent moodEvent = new MoodEvent(mood, null, null, null, null);
        a.moodEvents.add(moodEvent);

        Mood replaceMood = new Mood(MoodState.HAPPY);
        MoodEvent replaceMoodEvent = new MoodEvent(replaceMood, null, null, null, null);
        a.setMoodEvent(0, replaceMoodEvent);
        assertEquals(a.moodEvents.get(0), replaceMoodEvent);
    }

}
*/

