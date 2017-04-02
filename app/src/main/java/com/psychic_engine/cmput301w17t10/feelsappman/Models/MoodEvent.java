package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import android.location.Location;

import com.psychic_engine.cmput301w17t10.feelsappman.Enums.SocialSetting;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.EmptyMoodException;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jyuen1 on 2/27/17.
 * Commented by Alex Dong on 03/12/2017
 */
/**
 * Model class for a standard mood entry. Each mood event requires a Mood that is mandatory. Other
 * fields such as trigger, social setting, and pictures are optional features that the participant
 * would like to add.
 * @see Mood
 * @see SocialSetting
 * @see Photograph
 */
public class MoodEvent implements Serializable{
    private String id;
    private String moodOwner;
    private ArrayList<String> followers;
    private Mood mood;
    private String trigger;
    private SocialSetting socialSetting;
    private Date date;
    private Photograph picture;
    private MoodLocation location;

    // string array and split
    /**
     * Depending on the input that the system receives, each field aside from the mood and the date,
     * can be set to null (not inputted). Features such as picture and location require prior
     * permissions to access such as using other applications (Camera, Google Maps) and participant
     * intent.
     * @param mood
     * @param socialSetting
     * @param trigger
     * @param picture
     * @param location
     */
    public MoodEvent(Mood mood, SocialSetting socialSetting, String trigger, Photograph picture,
                     MoodLocation location) throws EmptyMoodException, TriggerTooLongException {
        this.id = "";
        setMood(mood);
        this.followers = new ArrayList<>();
        this.socialSetting = socialSetting;
        this.date = new Date();
        this.moodOwner = ParticipantSingleton.getInstance().getSelfParticipant().getLogin();
        setTrigger(trigger);
        this.picture = picture;
        this.location = location;
    }

    /**
     * Getter for Id given
     * @return
     */
    public String getId() { return this.id; }

    /**
     * Getter method to return the mood that was set in the mood event.
     * @see Mood
     * @return mood
     */
    public Mood getMood() { return this.mood; }

    /**
     * Getter method to return the social setting that was affiliated with the mood event
     * @see SocialSetting
     * @return socialSetting
     */
    public SocialSetting getSocialSetting() { return this.socialSetting; }

    /**
     * Getter method to return the trigger/reason of the mood event
     * @return trigger
     */
    public String getTrigger() { return this.trigger; }

    /**
     * Getter method to return the date that the mood event was created
     * @return date of mood event
     */
    public Date getDate() { return this.date; }

    /**
     * Getter method to return the picture that the participant has set. The picture must have been
     * validated to be less than the limit required for elastic search in the future.
     * @see Photograph
     * @return picture chosen by the participant
     */
    public Photograph getPicture() { return this.picture; }

    /**
     * Getter method to return the location where the mood event took place. The location will not
     * be set if the participant did not permit the system to record the location.
     * @see Location
     * @return location as a Location class
     */
    public MoodLocation getLocation() { return this.location; }

    /**
     * Setter for uniqueID
     * @param id
     */
    public void setId(String id) { this.id = id; }
    /**
     * Setter method to set the mood to the parameter. Used when editing the mood event
     * @param mood
     */
    public void setMood(Mood mood) throws EmptyMoodException {
        if (mood == null) {
            throw new EmptyMoodException();
        }
        this.mood = mood;
    }

    /**
     * Setter method to set the social setting given. Used when editing the mood event.
     * @param socialSetting
     */
    public void setSocialSetting(SocialSetting socialSetting) { this.socialSetting = socialSetting; }

    /**
     * Setter method to set the reason for the mood event. The reason for the mood event must be
     * simple and short, and thus must be less than 20 character and 3 words long. Failure to meet
     * these requirements will cause the system to reject the edit/add request for the mood event.
     * @param trigger
     * @throws TriggerTooLongException
     */
    public void setTrigger(String trigger) throws TriggerTooLongException {
        if (trigger.length() > 20 || trigger.trim().split("\\s+").length > 3) {
            throw new TriggerTooLongException();
        }
        this.trigger = trigger;
    }

    /**
     * Setter method to set the date that the mood event was created.
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Setter method to set the picture that the participant has chosen so long that the picture
     * is within the correct size.
     * @param picture
     */
    public void setPicture(Photograph picture) {
        this.picture = picture;
    }

    /**
     * Setter method to set the location where the participant has registered their mood event.
     * Will only be called when the participant has requested that the location should be saved
     * along with the current mood event.
     * @param location
     */
    public void setLocation(MoodLocation location) {
        this.location = location;
    }

    /**
     * Method to display on the history tab the general description of your mood, as well as the
     * date that the mood has been last created/editted
     * @return
     */
    public String toString(){
        return moodOwner + " feels "+ this.mood.getMood().toString() + " | " + this.getDate() ;
    }

    /**
     * Getter method to get all of the participants that follow this mood event
     * @return eventFollower list for the mood event
     */
    // "adder method" will be put in the mood controller
    public ArrayList<String> getEventFollowers() {
        return this.getEventFollowers();
    }

}