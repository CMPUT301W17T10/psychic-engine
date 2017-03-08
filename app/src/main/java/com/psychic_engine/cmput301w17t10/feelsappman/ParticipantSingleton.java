package com.psychic_engine.cmput301w17t10.feelsappman;

import java.util.ArrayList;

/**
 * Created by jyuen1 on 3/8/17.
 */

public class ParticipantSingleton {
    private static ParticipantSingleton instance = null;

    private ArrayList<Participant> participantList;
    private Participant currentParticipant;
    private int participantCount;

    private ParticipantSingleton() {
        participantList = new ArrayList<Participant>();
        participantCount = 0;
    }

    public static ParticipantSingleton getInstance() {
        if (instance == null) {
            instance = new ParticipantSingleton();
        }
        return instance;
    }

    public ArrayList<Participant> getParticipantList() {
        return participantList;
    }

    public Participant getCurrentParticipant() {
        return currentParticipant;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void addParticipant(Participant participant) {
        participantList.add(participant);
        participantCount++;
    }

    public void removeParticipant(Participant participant) {
        participantList.remove(participantList.indexOf(participant));
        participantCount--;
    }

    public boolean isLoaded() {
        return instance == null ? false : true;
    }

    public void loadInstance(ParticipantSingleton instance) {
        this.instance = instance;
    }

}
