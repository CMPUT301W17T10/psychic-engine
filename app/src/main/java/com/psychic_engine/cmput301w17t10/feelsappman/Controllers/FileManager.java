package com.psychic_engine.cmput301w17t10.feelsappman.Controllers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Created by jyuen1 on 3/15/17.
 * Original code by adong
 * Comments by adong on 3/28/2017.
 */

public class FileManager {
    private static final String FILENAME = "file.sav";
    private static ParticipantSingleton instance;


    // TODO: GSON does not properly load files. Will crash the application sometimes

    /**
     * This method will extract the details from the FILENAME from JSON to the participant singleton.
     * Upon successful execution of the method, the instance that is current in the running program
     * will be set as the previous instance that was saved upon exit.
     */
    public static void loadFromFile(Context ctx) {
        try {
            FileInputStream fis = ctx.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html Jan-21-2016
            Type type = new TypeToken<ParticipantSingleton>() {}.getType();
            instance = gson.fromJson(in, type);
            if (instance != null) {
                instance.setInstance(instance);
            }
            else
                instance = ParticipantSingleton.getInstance();
        } catch (FileNotFoundException e) {
            instance = ParticipantSingleton.getInstance();
        }
    }

    /**
     * Method to save the instance for future use upon destruction of the activity. The singleton
     * instance will contain all of the participants activity (moods, signing up, etc.)
     */
    public static void saveInFile(Context ctx) {
        try {
            FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(ParticipantSingleton.getInstance(), out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
