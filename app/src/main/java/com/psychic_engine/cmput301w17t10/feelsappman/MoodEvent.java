package com.psychic_engine.cmput301w17t10.feelsappman;

import android.location.Location;

import java.util.Date;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class MoodEvent {
    private Mood mood;
    private String trigger;
    private SocialSetting socialSetting;
    private Date date;
    private Photograph picture;
    private Location location;

    public MoodEvent(Mood mood, SocialSetting socialSetting, String trigger, Photograph picture, Location location) {
        this.mood = mood;
        this.socialSetting = socialSetting;
        this.date = new Date();
        this.trigger = trigger;
        this.picture = picture;
        this.location = location;
    }

    public Mood getMood() { return this.mood; }
    public SocialSetting getSocialSetting() { return this.socialSetting; }
    public String getTrigger() { return this.trigger; }
    public Date getDate() { return this.date; }
    public Photograph getPicture() { return this.picture; }
    public Location getLocation() { return this.location; }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public void setSocialSetting(SocialSetting socialSetting) { this.socialSetting = socialSetting; }

    public void setTrigger(String trigger) throws triggerTooLongException {
        this.trigger = trigger;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPicture(Photograph picture) {
        this.picture = picture;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}