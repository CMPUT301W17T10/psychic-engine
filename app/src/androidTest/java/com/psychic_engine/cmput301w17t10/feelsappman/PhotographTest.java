package com.psychic_engine.cmput301w17t10.feelsappman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;

import com.psychic_engine.cmput301w17t10.feelsappman.Activities.MyProfileActivity;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Photograph;

import java.io.File;

/**
 * Created by hnkhan on 2/27/17. Editted by adong
 */

/**
 * Photograph test to determine the picture functions are working properly. You require a picture
 * that is stored locally such as Pierre's laptop that contains a picture that is too big, as well
 * as the directory.
 */
public class PhotographTest extends ActivityInstrumentationTestCase2 {

        public PhotographTest() {
            super(MyProfileActivity.class);
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
            String jpgPath = path + "/Download/Untitled.jpg";   // TODO use own image
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
            String jpgPath = path + "/Download/792d0f374edb70e4cdf78365b9747aa5.jpg";
            File file = new File(jpgPath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            Photograph testPhotograph = new Photograph(bitmap);

            assertTrue(testPhotograph.getLimitSize());
        }
    }
