package com.psychic_engine.cmput301w17t10.feelsappman;

import android.icu.text.MessagePattern;

import junit.framework.TestCase;

/**
 * Created by jspun on 2/27/17.
 */

public class ParticipantTest extends TestCase {
/*
    public void testgetmoodlist(){
        Participant a = new Participant("first");
        Mood mood = new Mood(MoodState.Anger);
        MoodEvent moodEvent = new MoodEvent(mood);

        a.moodEvents.add(moodEvent);

        assertEquals(a.moodEvents,a.getMoodList());



    }
    */
    public void testgetFollowers(){
        Participant a = new Participant("first");
        Participant b = new Participant("second");

        a.followers.add(b);

        assertEquals(a.followers,a.getFollowers());
    }

    public void testgetFollowing(){
        Participant a = new Participant("first");
        Participant b = new Participant("second");

        a.following.add(b);

        assertEquals(a.following,a.getFollowing());
    }

    public void testgetPendingRequest(){
        Participant a = new Participant("first");
        Participant b = new Participant("second");

        a.pendingRequests.add(b);

        assertEquals(a.pendingRequests,a.getPendingRequests());
    }
}
