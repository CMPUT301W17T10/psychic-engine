package com.psychic_engine.cmput301w17t10.feelsappman.Exceptions;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class TriggerTooLongException extends Exception{
    public TriggerTooLongException() {}

    public TriggerTooLongException(String detailMessage) {
        super(detailMessage);
    }
}
