package com.example.brendon.productivityapp;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.provider.Settings;
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
    public static final String PREFS_NAME = "Settings";
    public static final String EXTRA_GOAL = "GOAL";
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;
    BackgroundJobService backgroundService;
    private static int jobId = 0;
    Settings settings;

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


        SharedPreferences settingsPreferences = getSharedPreferences(PREFS_NAME, 0);
        String json = settingsPreferences.getString(PREFS_NAME, null);
        if(json != null) {
            Gson gson = new Gson();

            settings = gson.fromJson(json, Settings.class);
        }
        else
            settings = new Settings();

        //startBackgroundService();
    }

    public void startBackgroundService() {
        ComponentName mServiceComponent = new ComponentName(this, BackgroundJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId++, mServiceComponent);
        builder.setMinimumLatency(60 * 1000);
        builder.setOverrideDeadline(600 * 1000);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        JobScheduler jobScheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
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



    // Checks if the user has granted permission to the app
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("You must allow the app permission to access Usage Stats.")
               .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(
                        new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                       "This app will not work without Usage Stats.",
                                       Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
        /*Toast.makeText(getApplicationContext(),
                "You must allow the app permission to access Usage Stats./nOpening System Settings.",
                Toast.LENGTH_LONG).show();

        startActivityForResult(
                new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS),
                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);*/
    }

    public void startMessageActivity(View view) {
        // We delcare an intent to start a new Activity
        Intent intent = new Intent(this, MessagingActivity.class);

        // Now, we start a new Activity
        startActivity(intent);
    }

    public void startFirstTime(View view) {
        Intent intent = new Intent(this, IntroductionMessageActivity.class);

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

    public void startPlanActivity(View view) {
        Intent intent = new Intent(this, EditPlan.class);

        startActivity(intent);
    }

    public void startSetApps(View view) {
        Intent intent = new Intent(this,FirstTimeActivity.class);

        startActivity(intent);
    }

    public void testTimeTracking(View view) {
        Intent intent = new Intent(this, TimeTrackingService.class);

        startActivity(intent);
    }

    public void startViewGoalActivity(View view) {
        Intent intent = new Intent(this, GoalView.class);

        startActivity(intent);
    }



}
