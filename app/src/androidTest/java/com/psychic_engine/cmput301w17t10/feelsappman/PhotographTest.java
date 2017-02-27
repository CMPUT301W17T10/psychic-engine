package com.psychic_engine.cmput301w17t10.feelsappman;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import java.lang.Object;
/**
 * Created by hnkhan on 2/27/17. Editted by adong
 */


public class PhotographTest extends TestCase {

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
        if (testPhotograph.getImage()!=null) {
            testPhotograph.deleteImage();
            assertTrue(testPhotograph.getImage()==null);
        }
        else {
            assertTrue(false);
        }

    }
}
