package com.psychic_engine.cmput301w17t10.feelsappman;

import android.location.Location;

import java.util.Date;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class MoodEvent {
    private Mood mood;
    private String trigger;
    private Date date;
    private Photograph picture;
    private Location location;

    public MoodEvent(Mood mood, String trigger, Photograph picture, Location location) {
        this.mood = mood;
        this.date = new Date();
        this.trigger = trigger;
        this.picture = picture;
        this.location = location;
    }

    public Mood getMood() { return mood; }
    public String getTrigger() { return trigger; }
    public Date getDate() { return date; }
    public Photograph getPicture() { return picture; }
    public Location getLocation() { return location; }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

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