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

    // testSearchParticipant goes into removeParticipants?? - alex
    public void testSearchParticipant() {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        assertEquals("index or error", instance.searchParticipant("meep"), instance.getParticipantList().get(1));
        assertEquals(instance.searchParticipant("notexist"), null);
    }

}
