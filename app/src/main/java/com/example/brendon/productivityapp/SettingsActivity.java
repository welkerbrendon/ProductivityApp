package com.example.brendon.productivityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "savedSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String settingsJson = sharedPreferences.getString("Settings", null);

        if (settingsJson != null) {
            Gson gson = new Gson();
            Settings settings = gson.fromJson(settingsJson, Settings.class);

            // Set checkboxes to match settings
        }
    }

    public void goNext(View view){

        Settings settings = new Settings();

        CheckBox chk_notifications = (CheckBox) findViewById(R.id.chk_notifications);
        if (chk_notifications.isChecked()) { settings.setNotifications(true); }

        CheckBox chk_weekly_goal_reminder = (CheckBox) findViewById(R.id.chk_weekly_goal_reminder);
        if (chk_weekly_goal_reminder.isChecked()) { settings.setWeeklyGoalReminder(true); }

        CheckBox chk_daily_plan_reminder = (CheckBox) findViewById(R.id.chk_daily_plan_reminder);
        if (chk_daily_plan_reminder.isChecked()) { settings.setDailyPlanReminder(true); }

        CheckBox chk_weekly_plan_reminder = (CheckBox) findViewById(R.id.chk_weekly_plan_reminder);
        if (chk_weekly_plan_reminder.isChecked()) { settings.setWeeklyPlanReminder(true); }

        CheckBox chk_lock_out = (CheckBox) findViewById(R.id.chk_lock_out);
        if (chk_lock_out.isChecked()) { settings.setLockOut(true); }

        CheckBox chk_auto_send_data = (CheckBox) findViewById(R.id.chk_auto_data_sending);
        if (chk_auto_send_data.isChecked()) { settings.setAutoDataSending(true); }


        EditText break_length = (EditText) findViewById(R.id.editText2);
        String input = break_length.getText().toString();
        int breakLength;

        if (input.isEmpty())
            breakLength = 0;
        else
            breakLength = Integer.parseInt(input);

        settings.setBreakLength(breakLength);

        settings.saveToSharedPreferences(this);

        Intent intent;

        if(settings.isFirstTime()){
            intent = new Intent(this, EditPlan.class);
        }
        else{
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
    }
}
