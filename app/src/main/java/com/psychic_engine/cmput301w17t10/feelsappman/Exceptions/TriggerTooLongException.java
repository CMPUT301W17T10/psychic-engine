package com.psychic_engine.cmput301w17t10.feelsappman.Exceptions;

/**
 * Created by jyuen1 on 2/27/17.
 */

/**
 * An exception that is thrown whenever the participant issues a trigger/readon that is beyond the
 * limit of 20 letters or 3 words.
 */
public class TriggerTooLongException extends Exception{
    public TriggerTooLongException() {}

    public TriggerTooLongException(String detailMessage) {
        super(detailMessage);
    }
}
