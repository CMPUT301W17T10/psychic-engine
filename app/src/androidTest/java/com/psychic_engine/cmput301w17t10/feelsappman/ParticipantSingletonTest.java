package com.psychic_engine.cmput301w17t10.feelsappman;

import android.icu.text.MessagePattern;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by adong on 3/8/17.
 */

// Note: Instances do carry over when you run the tests, singular tests will not work anymore
public class ParticipantSingletonTest extends ActivityInstrumentationTestCase2{

    public ParticipantSingletonTest() {
        super(LoginActivity.class);
    }

    public void testIsLoaded() {
        ParticipantSingleton instance = null;
        assertEquals("instance is not null", instance.isLoaded(), null);
    }
    public void testGetInstance() {
        ParticipantSingleton instance;
        instance = ParticipantSingleton.getInstance();
        assertTrue(instance != null);
        assertTrue(instance.isLoaded() != null);
    }
    public void testSetSelfParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        Participant secondParticipant = new Participant("dong");
        instance.setSelfParticipant(secondParticipant);
        assertTrue(instance.getSelfParticipant() != null);
    }

    public void testGetSelfParticipant() {
        Participant thirdParticipant = new Participant("alex");
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        instance.setSelfParticipant(thirdParticipant);
        assertEquals(instance.getSelfParticipant().getLogin(), thirdParticipant.getLogin());
    }

    public void testAddParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        Participant fourthParticipant = new Participant("oi");
        assertTrue(instance.addParticipant(fourthParticipant));
    }

    public void testGetParticipantCount() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        Participant fifthParticipant = new Participant("meep");
        assertTrue(instance.getParticipantCount() == 1);
        instance.addParticipant(fifthParticipant);
        assertTrue(instance.getParticipantCount() == 2);
    }

    public void testGetParticipantList() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        ArrayList<Participant> participantArrayList = instance.getParticipantList();
        assertEquals(participantArrayList, instance.getParticipantList());
        assertEquals(participantArrayList.get(1), instance.getParticipantList().get(1));
        assertEquals(participantArrayList.get(0), instance.getParticipantList().get(0));
    }

    // Currently not working 
    public void testRemoveParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue(instance.getParticipantCount() == 2);
        Participant aParticipant = new Participant("I dont exist");
        assertFalse(instance.removeParticipant(aParticipant));
        Participant someParticipant = new Participant("meep");
        assertTrue("failed remove",instance.removeParticipant(someParticipant));
        assertFalse("count is 2", instance.getParticipantCount() == 2);
        assertEquals(instance.getParticipantList().indexOf("meep"), -1);
    }



}
