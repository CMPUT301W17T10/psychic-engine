package com.psychic_engine.cmput301w17t10.feelsappman;

import java.util.ArrayList;

/**
 * Created by adong on 3/9/17.
 */

public class ModelFrame <V extends ViewFrame>{
        private ArrayList<V> views;

        public ModelFrame() {
            views = new ArrayList<V>();
        }

        public void addView(V view) {
            if (!views.contains(view)) {
                views.add(view);
            }
        }

        public void deleteView(V view) {
            views.remove(view);
        }

        public void notifyViews() {
            for (V view : views) {
                view.update(this);
            }
        }
}

