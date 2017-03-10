package com.psychic_engine.cmput301w17t10.feelsappman;

import android.icu.text.MessagePattern;

import junit.framework.TestCase;

/**
 * Created by jspun on 2/27/17.
 */

public class ParticipantTest extends TestCase {

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

    // Not really required for the Participant model
    /*
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
    */
}
