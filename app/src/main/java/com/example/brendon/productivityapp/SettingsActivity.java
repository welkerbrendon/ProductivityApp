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

    public static final String PREFS_NAME = "Settings";

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
            CheckBox chk_notifications = (CheckBox) findViewById(R.id.chk_notifications);
            chk_notifications.setChecked(settings.isNotifications());

            CheckBox chk_weekly_goal_reminder = (CheckBox) findViewById(R.id.chk_weekly_goal_reminder);
            chk_weekly_goal_reminder.setChecked(settings.isWeeklyGoalReminder());

            CheckBox chk_daily_plan_reminder = (CheckBox) findViewById(R.id.chk_daily_plan_reminder);
            chk_daily_plan_reminder.setChecked(settings.isDailyPlanReminder());

            CheckBox chk_weekly_plan_reminder = (CheckBox) findViewById(R.id.chk_weekly_plan_reminder);
            chk_weekly_plan_reminder.setChecked(settings.isWeeklyPlanReminder());

            CheckBox chk_lock_out = (CheckBox) findViewById(R.id.chk_lock_out);
            chk_lock_out.setChecked(settings.isLockOut());

            CheckBox chk_auto_send_data = (CheckBox) findViewById(R.id.chk_auto_data_sending);
            chk_auto_send_data.setChecked(settings.isAutoDataSending());
        }
    }

    public void goNext(View view){

        Settings settings = new Settings();

        CheckBox chk_notifications = (CheckBox) findViewById(R.id.chk_notifications);
        settings.setNotifications(chk_notifications.isChecked());

        CheckBox chk_weekly_goal_reminder = (CheckBox) findViewById(R.id.chk_weekly_goal_reminder);
        settings.setWeeklyGoalReminder(chk_weekly_goal_reminder.isChecked());

        CheckBox chk_daily_plan_reminder = (CheckBox) findViewById(R.id.chk_daily_plan_reminder);
        settings.setDailyPlanReminder(chk_daily_plan_reminder.isChecked());

        CheckBox chk_weekly_plan_reminder = (CheckBox) findViewById(R.id.chk_weekly_plan_reminder);
        settings.setWeeklyPlanReminder(chk_weekly_plan_reminder.isChecked());

        CheckBox chk_lock_out = (CheckBox) findViewById(R.id.chk_lock_out);
        settings.setLockOut(chk_lock_out.isChecked());

        CheckBox chk_auto_send_data = (CheckBox) findViewById(R.id.chk_auto_data_sending);
        settings.setAutoDataSending(chk_auto_send_data.isChecked());

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
