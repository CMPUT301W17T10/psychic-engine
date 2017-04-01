package com.psychic_engine.cmput301w17t10.feelsappman.Exceptions;

/**
 * Created by Jen on 4/1/2017.
 */

/**
 * An exception that is thrown when the participant tries
 * to create a mood event without specifying a mood.
 */
public class EmptyMoodException extends Exception {
    public EmptyMoodException() {}

    public EmptyMoodException(String detailMessage) {
        super(detailMessage);
    }
}
