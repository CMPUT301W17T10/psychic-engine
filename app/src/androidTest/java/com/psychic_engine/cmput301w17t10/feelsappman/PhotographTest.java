package com.psychic_engine.cmput301w17t10.feelsappman;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by hnkhan on 2/27/17. Editted by adong
 */

public class PhotographTest extends ActivityInstrumentationTestCase2 {

        //TODO Temporary activity to be the added
        public PhotographTest() {
            super(SelfNewsFeedActvity.class);
        }

        public void testSetImage() {
            //assume photograph exists

        }

        public void testGetImage() {
            //assume photograph exists

            Photograph testPhotograph = new Photograph();

            testPhotograph.getImage(); //getImage gets the resource ID to the drawable
            assertTrue(testPhotograph.getImage() != null);
        }


        public void testDeleteImage() {
            //assume photograph exists
            Photograph testPhotograph = new Photograph();
            testPhotograph.getImage();
            if (testPhotograph.getImage() != null) {
                testPhotograph.deleteImage();
                assertTrue(testPhotograph.getImage() == null);
            } else {
                assertTrue(false);
            }

        }
    }
