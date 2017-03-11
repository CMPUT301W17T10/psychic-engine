package com.psychic_engine.cmput301w17t10.feelsappman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;

import java.io.File;

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

            //Taken from http://stackoverflow.com/questions/13095718/where-are-android-emulator-image-stored
            //March 10, 2017
            String path = Environment.getExternalStorageDirectory().getPath();
            String jpgPath = path + "/Download/Untitled.jpg";
            File file = new File(jpgPath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            Photograph testPhotograph = new Photograph(bitmap);

            testPhotograph.getImage(); //getImage gets the resource ID to the drawable
            assertTrue(testPhotograph.getImage() != null);
        }
        //NOTE THAT IMAGES ARE STORED LOCALLY, RUNS ON PIERRES' LAPTOP
        public  void testGetLimitSizeUnder() {
            //Taken from http://stackoverflow.com/questions/13095718/where-are-android-emulator-image-stored
            //March 10, 2017
            String path = Environment.getExternalStorageDirectory().getPath();
            String jpgPath = path + "/Download/Untitled.jpg";
            File file = new File(jpgPath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            Photograph testPhotograph = new Photograph(bitmap);

            assertTrue(testPhotograph.getLimitSize());
        }

        //NOTE THAT IMAGES ARE STORED LOCALLY, RUNS ON PIERRES' LAPTOP
        public  void testGetLimitSizeOver() {
            //Taken from http://stackoverflow.com/questions/13095718/where-are-android-emulator-image-stored
            //March 10, 2017
            String path = Environment.getExternalStorageDirectory().getPath();
            String jpgPath = path + "/Download/masterpiece.jpg";
            File file = new File(jpgPath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            Photograph testPhotograph = new Photograph(bitmap);

            assertFalse(testPhotograph.getLimitSize());
        }


//        public void testDeleteImage() {
//            //assume photograph exists
//            Photograph testPhotograph = new Photograph();
//            testPhotograph.getImage();
//            if (testPhotograph.getImage() != null) {
//                testPhotograph.deleteImage();
//                assertTrue(testPhotograph.getImage() == null);
//            } else {
//                assertTrue(false);
//            }
//
//        }
    }
