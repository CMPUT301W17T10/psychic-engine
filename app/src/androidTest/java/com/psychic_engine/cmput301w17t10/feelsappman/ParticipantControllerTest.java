package com.psychic_engine.cmput301w17t10.feelsappman;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.ArrayList;

/**
 * Created by adong on 2017-03-30.
 * Comments by adong on 03-30-2017
 */

/**
 * ParticipantControllerTest attempts to determine proper functionality of its methods. Note that
 * since the methods incorporates elastic search, the servers will also be updated as well. It is
 * important to remember if the test cases run here that data in the server may not be what you
 * expect.
 */
public class ParticipantControllerTest extends TestCase {
    private ParticipantSingleton instance = ParticipantSingleton.getInstance();
    private ElasticParticipantController.AddParticipantTask apt = new ElasticParticipantController
            .AddParticipantTask();
    @Before
    public void setUp() {
        Participant testParticipant = new Participant("ControllerTest");
        apt.execute(testParticipant);
    }

    public void test1_CheckUniqueParticipant() {
        // should find the participant thus return true else fail
        if (!ParticipantController.checkUniqueParticipant("ControllerTest")) {
            assertTrue(true);
        } else {
            assertTrue("Fail", false);
        }

        // should not find participant  else fail
        if (ParticipantController.checkUniqueParticipant("ControllerTestDummy")) {
            assertTrue(true);
        } else {
            assertTrue("Fail", false);
        }
    }

    /**
     * Tests the sync up portion of the participant list stored in the singleton. Highly variable
     * results will cause occasional failure, thus a small section of the list will be utilized
     * to demonstrate main functionality
     */
    public void test2_updateSingletonList() {
        ArrayList<Participant> dummyList = new ArrayList<>();
        Participant dummy1 = new Participant("UpdateDummy1");
        Participant dummy2 = new Participant("UpdateDummy2");
        Participant dummy3 = new Participant("UpdateDummy3");
        Participant dummy4 = new Participant("UpdateDummy4");

        dummyList.add(dummy1);
        dummyList.add(dummy2);
        dummyList.add(dummy3);
        dummyList.add(dummy4);

        // add test users into the server
        apt.execute(dummy1, dummy2, dummy3, dummy4);

        // attempt to obtain all of these participants within the singleton list
        ParticipantController.updateSingletonList();

        for (Participant found : dummyList) {
            if (instance.getParticipantList().contains(found)) {
                assertTrue(true);
            } else {
                assertTrue("Not all found", false);
            }
        }


    }
}
