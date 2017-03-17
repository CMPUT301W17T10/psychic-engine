package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import android.util.Log;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.LoginActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ModelFrame;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by adong on 2/27/17.
 */

/**
 * Participant model to describe the attributes of the participant. Each participant upon signup
 * will hold their own personal arrays of mood events, followers, following, and pending requests.
 * Currently the last three are out of service until later development.
 * @see LoginActivity
 */
public class Participant extends ModelFrame {
    private String login;
    private MoodEvent mostRecentMoodEvent;
    private ArrayList<MoodEvent> moodEvents;
    private ArrayList<Participant> followers;
    private ArrayList<Participant> following;
    private ArrayList<Participant> pendingRequests;


    /**
     * Everytime the participant is initialized, we will save their name into the system. Since
     * the account is new, they will have no mood events, no followers, no following, and no
     * pending requests.
     * @param loginName
     */
    public Participant(String loginName) {
        this.login = loginName;
        this.moodEvents = new ArrayList<MoodEvent>();
        this.followers = new ArrayList<Participant>();
        this.following = new ArrayList<Participant>();
        this.pendingRequests = new ArrayList<Participant>();
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
    public ArrayList<Participant> getFollowers() {
        return this.followers;
    }

    /**
     * Getter method to get the following list, Will return NULL pointer if called when empty.
     * @return
     */
    public ArrayList<Participant> getFollowing() {
        return this.following;
    }

    /**
     * Getter method to get the pending requests list. Will return NULL pointer if called empty.
     * @return
     */
    public ArrayList<Participant> getPendingRequests() {
        return this.pendingRequests;
    }

    /**
     * Getter for mostRecentMoodEvent
     * @return
     */
    public MoodEvent getMostRecentMoodEvent() { return this.mostRecentMoodEvent; }


    /**
     * Adds a new mood event to the mood event list
     * @param moodEvent
     */
    public void addMoodEvent(MoodEvent moodEvent) {
        if (moodEvents.isEmpty()) {
            Log.d("Empty", "MoodEvents is empty for "+ login);
        }

        // update most recent mood event
        if (mostRecentMoodEvent == null) {
            mostRecentMoodEvent = moodEvent;
        } else if (moodEvent.getDate().after(mostRecentMoodEvent.getDate())) {
            mostRecentMoodEvent = moodEvent;
        }

        moodEvents.add(moodEvent);
        Log.d("Success", "Successful addition of mood event");
        Log.d("Added", this.moodEvents.get(0).getMood().getMood().toString());
    }


    /**
     * Set the most recent mood event
     * @param moodEvent
     */
    public void setMostRecentMoodEvent(MoodEvent moodEvent) {
        this.mostRecentMoodEvent = moodEvent;
    }
}


