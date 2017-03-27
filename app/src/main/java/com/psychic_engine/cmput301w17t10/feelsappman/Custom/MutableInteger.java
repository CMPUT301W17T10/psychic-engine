package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

/**
 * Created by jyuen1 on 3/27/17.
 */

// Taken from http://blog.pengyifan.com/most-efficient-way-to-increment-a-map-value-in-java-only-search-the-key-once/ on 3/27/17
public class MutableInteger {

    private int val;

    public MutableInteger(int val) {
        this.val = val;
    }

    public int get() {
        return val;
    }

    public void set(int val) {
        this.val = val;
    }

    public float toFloat() {
        return (float) val;
    }
}
