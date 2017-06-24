package com.example.brendon.productivityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class EditGoalActivity extends AppCompatActivity {
    Goal userGoal = new Goal();
    TextView editGoalHeader;
    EditText hoursEditor;
    EditText minutesEditor;
    Settings settings;

    public static final String PREFS_NAME = "Settings";
    public static final String PREFS_GOAL_NAME = "savedGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);
        settings = Settings.getInstance(this);
        editGoalHeader = (TextView) findViewById(R.id.textViewPromptGoal);
        hoursEditor = (EditText) findViewById(R.id.editHours);
        minutesEditor = (EditText) findViewById(R.id.editMinutes);

        // Deserialize the Goal from the Extra we will receive
        Intent intent = getIntent();

        Gson gson = new Gson();
        String jsonGoal = intent.getStringExtra(GoalView.GOAL_EXTRA);
        if(gson.fromJson(jsonGoal, Goal.class) != null)
            userGoal = gson.fromJson(jsonGoal, Goal.class);
    }

    public void updateGoal() {
    }

    public void startGoalView(View view) {
        Intent intent = new Intent(this, GoalView.class);

        startActivity(intent);
    }

    public void goNext(View view) {
        if (userGoal != null)
            userGoal.saveToSharedPreferences(this);

        settings = Settings.getInstance(this);
        /*SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String settingsJson = sharedPreferences.getString("Settings", null);

        if (settingsJson != null) {
            Gson gson = new Gson();
            settings = gson.fromJson(settingsJson, Settings.class);
        }
        else
            settings = new Settings();*/

        Intent intent;
        if (settings.isFirstTime()) {
            intent = new Intent(this, SettingsActivity.class);
        } else {
            intent = new Intent(this, GoalView.class);
        }
        startActivity(intent);
    }
}
