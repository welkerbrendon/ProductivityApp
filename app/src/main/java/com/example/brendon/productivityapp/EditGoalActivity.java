package com.example.brendon.productivityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class EditGoalActivity extends AppCompatActivity {
    Goal userGoal;
    TextView editGoalHeader;
    EditText hoursEditor;
    EditText minutesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        editGoalHeader = (TextView) findViewById(R.id.textViewPromptGoal);
        hoursEditor = (EditText) findViewById(R.id.editHours);
        minutesEditor = (EditText) findViewById(R.id.editMinutes);

        // Deserialize the Goal from the Extra we will receive
        Intent intent = getIntent();

        Gson gson = new Gson();
        String jsonGoal = intent.getStringExtra(GoalView.GOAL_EXTRA);
        userGoal = gson.fromJson(jsonGoal, Goal.class);
    }

    public void updateGoal() {

    }

    public void startGoalView(View view) {
        Intent intent = new Intent(this, GoalView.class);

        startActivity(intent);
    }
}
