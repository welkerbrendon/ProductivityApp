package com.example.brendon.productivityapp;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Essentially the "Home Screen" of the app.
 * The user will be able to access their different Goals
 * The user will be able to access a changeSettings activity.
 * This activity will show a graphic charting the times that
 * the user has achieved their goal.
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "savedSettings";
    public static final String EXTRA_GOAL = "GOAL";
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;

    /**
     * This function will be instantiated when the activity is created.
     * <p>
     *     Will check if UsageStats permissions are enabled.  If they
     *     are not enabled, it will request the permission to be enabled.
     * </p>
     * <p>
     *     Will load the settings for the app.
     * </p>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user has enabled Usage Stats. If not, request it
        if (!hasPermission())
            requestPermission();

        /* This is the code that you need to use to load settings.
           I am not sure where you want me to put it, so I'll just
           leave it here */
        //Loading settings
        SharedPreferences settingsPref = getSharedPreferences(PREFS_NAME, 0);

        //Deserializing
        Gson gson = new Gson();
        String json = settingsPref.getString("Settings", "");
        Settings settings = gson.fromJson(json, Settings.class);

        scheduleAlarm();
    }

    protected void onPause() {
        super.onPause();

        /* This is the code that you need to use to save settings.
           I am not sure where you want me to put it, so I'll just
           leave it here */

        //Serializing settings
        Settings settings = new Settings();
        Gson gson = new Gson();
        String json = gson.toJson(settings);

        //Saving settings in shared preferences
        SharedPreferences settingsPref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor settingEditor = settingsPref.edit();
        settingEditor.putString("Settings", json);

        Log.d(TAG, "JSON saved: " + json);
        String toGetJson = "";


        //Commit edits
        settingEditor.commit();

        Log.d(TAG, settingsPref.getString("Settings", toGetJson));
    }

    public void scheduleAlarm() {
        // Make an Intent to send to the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        Log.d(TAG, "In scheduleAlarm()");

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstKickoffTime = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                firstKickoffTime, AlarmManager.INTERVAL_HOUR, pendingIntent);

        Log.d(TAG, "Alarm scheduled");
    }

    // Checks if the user has granted permission to the app
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestPermission() {
        Toast.makeText(getApplicationContext(),
                "You must allow the app permission to access Usage Stats./nOpening System Settings.",
                Toast.LENGTH_SHORT).show();

        startActivityForResult(
                new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
    }

    public void startMessageActivity(View view) {
        // We delcare an intent to start a new Activity
        Intent intent = new Intent(this, MessagingActivity.class);

        // Now, we start a new Activity
        startActivity(intent);
    }

    public void startFirstTimeActivity(View view) {
        Intent intent = new Intent(this, FirstTimeActivity.class);

        startActivity(intent);
    }

    public void startStatsActivity(View view) {
        Intent intent = new Intent(this, StatsActivity.class);

        startActivity(intent);
    }

    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        startActivity(intent);
    }

}
