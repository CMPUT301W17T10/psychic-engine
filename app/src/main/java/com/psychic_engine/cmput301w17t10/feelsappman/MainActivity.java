package com.psychic_engine.cmput301w17t10.feelsappman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // testing CreateMoodActivity
        Intent intent = new Intent(this, CreateMoodActivity.class);
        startActivity(intent);

    }
}
