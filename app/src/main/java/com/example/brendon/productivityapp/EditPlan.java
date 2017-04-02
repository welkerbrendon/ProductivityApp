package com.example.brendon.productivityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class EditPlan extends AppCompatActivity {
    EditText textEditorPlan;

    public static final String PREFS_NAME = "Settings";
    public static final String PREFS_GOAL_NAME = "savedGoal";
    Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);
    }

    public void startMainActivity(View view) {

        SharedPreferences settingsPreferences = getSharedPreferences(PREFS_NAME, 0);
        String json = settingsPreferences.getString(PREFS_NAME, null);
        if(json != null) {
            Gson gson = new Gson();

            settings = gson.fromJson(json, Settings.class);
        }
        else
            settings = new Settings();

        settings.setFirstTime(false);

        settings.saveToSharedPreferences(this);

        // TEST - Pull from shared preferences right after pushing
        // TEST - Pull from shared preferences right after pushing
        // TEST - Pull from shared preferences right after pushing

        String jsonTest = settingsPreferences.getString(PREFS_NAME, null);
        Gson gson = new Gson();
        Settings settingsTest = gson.fromJson(json, Settings.class);

        // TEST - Pull from shared preferences right after pushing
        // TEST - Pull from shared preferences right after pushing
        // TEST - Pull from shared preferences right after pushing

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
