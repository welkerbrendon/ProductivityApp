package com.example.brendon.productivityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class GoalView extends AppCompatActivity {
    // !! Replace "goal" with actual filename set in FirstTimeActivity !!
    public final static String GOAL_PREFS_FILE = "goal";

    private ProgressBar unproductiveProgressBar;
    private TextView viewUnproductiveMessage;
    private Button buttonUpdateGoal;
    private Goal userGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_view);

        // Load the goal from SharedPreferences
        SharedPreferences goalPreferences = getSharedPreferences(GOAL_PREFS_FILE, 0);

        // Convert json string to Goal Object
        // !! Replace "goal" with actual value set in FirstTimeActivity !!
        Gson gson = new Gson();
        String jsonGoal = goalPreferences.getString(GOAL_PREFS_FILE, "goal");
        userGoal = gson.fromJson(jsonGoal, Goal.class);

        // Calculate the progress made on the goal
        long progress = calculateProgress(userGoal);

        String unproductiveMessage =
                String.format("You have used %f% of your unproductiveTime", (float) progress);

        viewUnproductiveMessage = (TextView) findViewById(R.id.textViewGoal);
        viewUnproductiveMessage.setText(unproductiveMessage);
    }

    public long calculateProgress(Goal userGoal) {
        long progress;
        long rawProgress;
        long unproductiveGoal = userGoal.time.getMilliseconds();
        long unproductiveActual = userGoal.time.getMilliseconds();

        rawProgress = unproductiveActual / unproductiveGoal;
        progress = rawProgress * 100;

        return progress;
    }

    public void startEditGoalActivity(View view) {
        Intent intent;
    }
}
