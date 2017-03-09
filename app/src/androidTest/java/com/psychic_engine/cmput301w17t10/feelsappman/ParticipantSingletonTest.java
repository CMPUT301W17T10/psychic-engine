package com.psychic_engine.cmput301w17t10.feelsappman;

import android.icu.text.MessagePattern;
import android.os.Debug;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.ArrayList;

/**
 * Created by adong on 3/8/17.
 */

// Note: Instances do carry over when you run the tests, singular tests will not work anymore
public class ParticipantSingletonTest extends ActivityInstrumentationTestCase2{

    public ParticipantSingletonTest() {
        super(LoginActivity.class);
        Log.d("ONCE","loginActivity once");
    }

    public void testIsLoaded() {
        ParticipantSingleton instance = null;
        assertNotNull("instance is null",instance.isLoaded());
    }
    public void testGetInstance() {
        ParticipantSingleton instance;
        instance = ParticipantSingleton.getInstance();
        assertTrue(instance != null);
        assertTrue(instance.isLoaded() != null);
    }


    public void testAddParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue(instance.addParticipant("oi"));
    }

    public void testGetParticipantCount() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue("count not 1", instance.getParticipantCount() == 1);
        instance.addParticipant("meep");
        assertTrue("count not 2", instance.getParticipantCount() == 2);
    }


    public void testGetParticipantList() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        ArrayList<Participant> participantArrayList = instance.getParticipantList();
        assertEquals(participantArrayList, instance.getParticipantList());
        assertEquals(participantArrayList.get(1), instance.getParticipantList().get(1));
        assertEquals(participantArrayList.get(1).getLogin(), "meep");
        assertEquals(participantArrayList.get(0), instance.getParticipantList().get(0));
    }

    // testSearchParticipant goes into removeParticipants?? - alex
    public void testSearchParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertEquals("index or error", instance.searchParticipant("meep"), instance.getParticipantList().get(1));
        assertEquals(instance.searchParticipant("exist"), null);
    }

    public void testSetSelfParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertFalse("aaa was set but DNE",instance.setSelfParticipant("aaa"));
        assertTrue("did not set 'oi' even if exists",instance.setSelfParticipant("oi"));
    }

    public void testGetSelfParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue("it is null",instance.getSelfParticipant() == null);
        instance.setSelfParticipant("oi");
        assertEquals("self participant not 'oi'",instance.getSelfParticipant().getLogin(), "oi");
    }

    public void testRemoveParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue("count not 2", instance.getParticipantCount() == 2);
        Log.d("1st", "first remove");
        assertFalse("did delete", instance.removeParticipant("I dont exist"));
        Log.d("2nd", "second remove");
        assertTrue("failed remove",instance.removeParticipant("meep"));
        assertTrue("count is not 1", instance.getParticipantCount() == 1);
        assertEquals(instance.getParticipantList().indexOf("meep"), -1);
    }
}
