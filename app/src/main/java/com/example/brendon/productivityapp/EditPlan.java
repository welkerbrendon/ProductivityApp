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

    public static final String PREFS_NAME = "savedSettings";
    public static final String PREFS_GOAL_NAME = "savedGoal";
    Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);
    }

    public void startMainActivity(View view) {

        SharedPreferences settingPreferences = getSharedPreferences(PREFS_NAME, 0);
        if(settingPreferences.contains(PREFS_NAME)) {
            String json = settingPreferences.getString(PREFS_NAME, null);

            Gson gson = new Gson();
            settings = gson.fromJson(json, Settings.class);
        }
        else
            settings = new Settings();

        settings.setFirstTime(false);

        settings.saveToSharedPreferences(this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loadPlan() {
        SharedPreferences goalPreferences = getSharedPreferences(PREFS_GOAL_NAME, 0);
        if(goalPreferences.contains(PREFS_GOAL_NAME)){
            Goal goal = (Goal) goalPreferences;
            String plan = goal.getPlan();
            textEditorPlan.setText(plan);
        }
    }
}
