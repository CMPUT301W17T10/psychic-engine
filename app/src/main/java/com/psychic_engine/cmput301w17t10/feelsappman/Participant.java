package com.psychic_engine.cmput301w17t10.feelsappman;

import java.util.ArrayList;

/**
 * Created by adong on 2/27/17.
 */

public class Participant {
    public String login;
    public ArrayList<MoodEvent> moodEvents;
    public ArrayList<Participant> followers;
    public ArrayList<Participant> following;
    public ArrayList<Participant> pendingRequests;


    public Participant(String loginName) {

        this.login = loginName;
        this.moodEvents = new ArrayList<MoodEvent>();
        this.followers = new ArrayList<Participant>();
        this.following = new ArrayList<Participant>();
        this.pendingRequests = new ArrayList<Participant>();
    }

    //public void addmoodevent(MoodEvent moodEvent){
      //  moodEvents.add(moodEvent);
    //}

    /*public void addfollowers(Participant participant){
        followers.add(participant);

    }*/
    public String getLogin() { return this.login;}

    public ArrayList<MoodEvent> getMoodList() {
        return this.moodEvents;
    }

    public ArrayList<Participant> getFollowers() {
        return this.followers;
    }

    public ArrayList<Participant> getFollowing() {
        return this.following;
    }

    public ArrayList<Participant> getPendingRequests() {
        return this.pendingRequests;
    }

    /**
     * replace old mood event with new mood event at index
     * @param index
     * @param moodEvent
     */
    public void setMood(int index, MoodEvent moodEvent) {
        moodEvents.set(index, moodEvent);
    }
}

