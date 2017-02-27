package com.psychic_engine.cmput301w17t10.feelsappman;

/**
 * Created by jyuen1 on 2/27/17.
 */

public class triggerTooLongException extends Exception{
    public triggerTooLongException() {}

    public triggerTooLongException(String detailMessage) {
        super(detailMessage);
    }
}
