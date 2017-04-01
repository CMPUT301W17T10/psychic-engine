package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.LoginActivity;

import java.util.ArrayList;

/**
 * Created by adong on 2/27/17.
 * Commented by adong
 */

/**
 * Participant model to describe the attributes of the participant. Each participant upon signup
 * will hold their own personal arrays of mood events, followers, following, and pending requests.
 * Currently the last three are out of service until later development. Participants will have
 * their own unique ID upon creation. Followers/Following are kept to their names. When required,
 * pull profile of the names that they would have picked
 * @see LoginActivity
 */
public class Participant {
    private String login;
    private MoodEvent mostRecentMoodEvent;
    private ArrayList<MoodEvent> moodEvents;
    private ArrayList<String> followers;
    private ArrayList<String> following;
    private ArrayList<String> pendingRequests;
    private String uniqueID;


    /**
     * Everytime the participant is initialized, we will save their name into the system. Since
     * the account is new, they will have no mood events, no followers, no following, and no
     * pending requests.
     * @param loginName
     */
    public Participant(String loginName) {
        this.login = loginName;
        this.moodEvents = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
        this.uniqueID = "";
    }
    public void setId(String id) {this.uniqueID = id;}
    /**
     * Getter method to get the ID that was set by the elastic search server
     */
    public String getId() {
        return this.uniqueID;
    }

    /**
     * Getter method to get the login
     * @return
     */
    public String getLogin() {return this.login;}

    /**
     * Getter method to get the mood list of the participant. Will return NULL pointer
     * if called when empty.
     * @return
     */
    public ArrayList<MoodEvent> getMoodList() {
        return this.moodEvents;
    }

    /**
     * Getter method to get the followers list. Will return NULL poiinter if called when empty.
     * @return
     */
    public ArrayList<String> getFollowers() {
        return this.followers;
    }

    /**
     * Getter method to get the following list, Will return NULL pointer if called when empty.
     * @return
     */
    public ArrayList<String> getFollowing() {
        return this.following;
    }

    /**
     * Getter method to get the pending requests list. Will return NULL pointer if called empty.
     * @return
     */
    public ArrayList<String> getPendingRequests() {
        return this.pendingRequests;
    }

    /**
     * Getter for mostRecentMoodEvent
     * @return
     */
    public MoodEvent getMostRecentMoodEvent() { return this.mostRecentMoodEvent; }

    /**
     * Set the most recent mood event
     * @param moodEvent
     */
    public void setMostRecentMoodEvent(MoodEvent moodEvent) {
        this.mostRecentMoodEvent = moodEvent;
    }
}


