package com.psychic_engine.cmput301w17t10.feelsappman;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.LoginActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;

/**
 * Created by adong on 3/8/17.
 */
// TODO: App crashes on reopening of app when trying to enter a name - alex
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
        assertTrue(instance.addParticipant("meep"));
        assertTrue("count not 2", instance.getParticipantCount() == 2);
    }


    public void testGetParticipantList() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        ArrayList<Participant> participantArrayList = new ArrayList<Participant>();
        participantArrayList.add(new Participant("oi"));
        participantArrayList.add(new Participant("meep"));
        assertEquals(participantArrayList.get(0).getLogin(), instance.getParticipantList().get(0).getLogin());
    }

    // testSearchParticipant goes into removeParticipants?? - alex
    public void testSearchParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertEquals("index or error", instance.searchParticipant("meep"), instance.getParticipantList().get(1));
        assertEquals(instance.searchParticipant("notexist"), null);
    }

    public void testSetSelfParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        Participant someParticipant = instance.searchParticipant("aaa");
        assertFalse("aaa was set but DNE",instance.setSelfParticipant(someParticipant));
        Participant anotherParticipant = instance.searchParticipant("oi");
        assertTrue("did not set 'oi' even if exists",instance.setSelfParticipant(anotherParticipant));
    }

    public void testGetSelfParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue("it is null",instance.getSelfParticipant() == null);
        instance.setSelfParticipant(instance.searchParticipant("oi"));
        assertEquals("self participant not 'oi'",instance.getSelfParticipant().getLogin(), "oi");
    }

    // Currently only removes its own account from the file
    // Later on if needed, will remove other participants such as followers
    /*
    public void testRemoveParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertTrue("count not 2", instance.getParticipantCount() == 2);
        assertTrue("failed remove",instance.removeParticipant());
        assertTrue("count is not 1", instance.getParticipantCount() == 1);
        Participant thisParticipant = instance.searchParticipant("oi");
        assertEquals(instance.getParticipantList().indexOf(thisParticipant), -1);
    }
    */
}
