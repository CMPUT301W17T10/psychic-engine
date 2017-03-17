package com.psychic_engine.cmput301w17t10.feelsappman.Enums;

/**
 * Created by adong on 2/27/17.
 */
public enum SocialSetting {
        ALONE,
        ONEOTHER,
        TWOTOSEVERAL,
        CROWD;

        @Override
        public String toString() {
                switch(this) {
                        case ALONE: return "Alone";
                        case ONEOTHER: return "One Other";
                        case TWOTOSEVERAL: return "Two To Several";
                        case CROWD: return "Crowd";
                        default: throw new IllegalArgumentException();
                }
        }

}
