package com.example.brendon.productivityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class EditGoalActivity extends AppCompatActivity {
    TextView editGoalHeader;
    EditText hoursEditor;
    EditText minutesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);
    }
}
