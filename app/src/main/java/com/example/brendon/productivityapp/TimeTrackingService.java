package com.example.brendon.productivityapp;

import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This service will run in the background when started.
 * @author Scott Nicholes
 *
 * <p>
 *     This service calculates the amount of time
 *     from the time it was last called to the
 *     time it is re-instantiated that apps
 *     have been in the foreground.
 * </p>
 */

public class TimeTrackingService extends IntentService {
    private static final String TAG = "TimeTrackingService";
    private static final String PREFS_UNPRODUCTIVE_TIME_FILE = "UnproductiveTimeFile";
    private static final String PREFS_APPS_FILE = "UnproductiveAppsFile";
    private static final String JSON_LIST_KEY = "key";
    private static final String UNPRODUCTIVE_TIME_KEY = "UnproductiveTime";

    /*
    Note:  For this service to work, I will need someone to:
    1. Make a list of the unproductive apps (probably done at start-time.
    2. Because the list of unproductive apps is in the Goal, load that list into the Goal.
    3. Serialize the Goal class into JSON (Or, make Goal extend Parcelable and then load it
    into an intent that way.  This is because I can use getParcelableArrayListExtra() in this
    class.)
    4. Put the JSON into an intent with putExtra() and then use startService() to inititate
    this service.

     */

    public static final String PREFS_NAME = "savedSettings";
    public static final String PREFS_GOAL_NAME = "savedGoal";

    private boolean displayed25;
    private boolean displayed50;
    private boolean displayed75;
    private Goal theGoal;
    private Time unprouctiveTime;
    private Settings settings;

    public TimeTrackingService() {
        super("TimeTrackingService");
        displayed25 = false;
        displayed50 = false;
        displayed75 = false;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Here, we will need to get the Goal from the workIntent.  This
        //  means that in whatever function we call the workIntent,
        //  we must use setData() to put a JSON string of the Goal we want
        //  to modify.
        Log.d(TAG, "In Service");



        // Get the current (old) unproductive Time from Shared Preferences
        SharedPreferences unproductiveTimePreferences =
                getSharedPreferences(PREFS_UNPRODUCTIVE_TIME_FILE, 0);
        unprouctiveTime.setMilliseconds(unproductiveTimePreferences.getLong(UNPRODUCTIVE_TIME_KEY, 0));

        // Call calculate method to start calculation of unproductive time spent
        calculateUnproductiveTimeSpent();

        Time checkTime;
        checkTime = unprouctiveTime;
        saveUnproductiveTime();

        SharedPreferences settingsPreferences = getSharedPreferences(PREFS_NAME, 0);
        if(settingsPreferences.contains(PREFS_NAME))
            settings = (Settings) getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences goalPreferences = getSharedPreferences(PREFS_GOAL_NAME, 0);
        if(goalPreferences.contains(PREFS_GOAL_NAME))
            theGoal = (Goal) getSharedPreferences(PREFS_GOAL_NAME, 0);
        notifications(checkTime);
    }

    public void saveUnproductiveTime() {
        SharedPreferences unproductiveTimePreferences =
                getSharedPreferences(PREFS_UNPRODUCTIVE_TIME_FILE, 0);

        SharedPreferences.Editor unproductiveTimeEditor = unproductiveTimePreferences.edit();
        unproductiveTimeEditor.putLong(UNPRODUCTIVE_TIME_KEY ,unprouctiveTime.getMilliseconds());

        Log.d(TAG, "Unproductive Time Saved.  New Time: " + unprouctiveTime.getMilliseconds());
        unproductiveTimeEditor.commit();
    }

    public void calculateUnproductiveTimeSpent() {
        long tempUnproductiveTime = 0;

        // Update unproductiveTime

        // Create CustomUsageStats Object
        CustomUsageStats usageStatsAccessor = new CustomUsageStats();

        // Call getUsageStatsList
        List<UsageStats> allAppsList = usageStatsAccessor.getUsageStatsList(this);

        // Compare the NAMES (or PackageNames) to SharedPreferences List of Unproductive Apps
        SharedPreferences appsPreferences = getSharedPreferences(PREFS_APPS_FILE, 0);

        // We will retrieve the unproductiveAppsList by JSON
        String jsonList = appsPreferences.getString(JSON_LIST_KEY, null);
        List<UsageStats> unproductiveAppsList = new Gson().fromJson(jsonList,
                new TypeToken<List<UsageStats>>() {
                }.getType());

        // For all apps that match the name, get their timeInMilli and += to unproductiveTime.
        for (int i = 0; i < allAppsList.size(); i++) {
            if(unproductiveAppsList.contains(allAppsList.get(i))) {
                tempUnproductiveTime += allAppsList.get(i).getTotalTimeInForeground();
            }
        }

        // Now, update unproductiveTime
        unprouctiveTime.setMilliseconds(tempUnproductiveTime);

        // With the new unproductiveTime variable, it is now ready to be checked against
        //  notifications(checkTime);

    }

    public void notifications(Time checkTime) {
        if(settings.isNotifications() && !displayed25 && theGoal.getTime().getMilliseconds()*.25 <= unprouctiveTime.getMilliseconds()) {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("Message", "You have used up at least 25% of your goal time on unproductive apps.");
            intent.putExtra("Goal Hours", theGoal.getTime().getHours());
            intent.putExtra("Goal Minutes", theGoal.getTime().getMinutes());
            intent.putExtra("Goal Seconds", theGoal.getTime().getSeconds());
            intent.putExtra("Unproductive Hours", unprouctiveTime.convertMilliToHours(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Minutes", unprouctiveTime.convertMilliToMinutes(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Seconds", unprouctiveTime.convertMilliToSeconds(unprouctiveTime.getMilliseconds()));
            displayed25 = true;
            startActivity(intent);
        }
        else if(settings.isNotifications() && !displayed50 && theGoal.getTime().getMilliseconds()*.5 <= unprouctiveTime.getMilliseconds()){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("Message", "You have used up at least 50% of your goal time on unproductive apps.");
            intent.putExtra("Goal Hours", theGoal.getTime().getHours());
            intent.putExtra("Goal Minutes", theGoal.getTime().getMinutes());
            intent.putExtra("Goal Seconds", theGoal.getTime().getSeconds());
            intent.putExtra("Unproductive Hours", unprouctiveTime.convertMilliToHours(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Minutes", unprouctiveTime.convertMilliToMinutes(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Seconds", unprouctiveTime.convertMilliToSeconds(unprouctiveTime.getMilliseconds()));
            displayed50 = true;
            startActivity(intent);
        }
        else if(settings.isNotifications() && !displayed75 && theGoal.getTime().getMilliseconds()*.75 <= unprouctiveTime.getMilliseconds()){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("Message", "You have used up at least 75% of your goal time on unproductive apps!");
            intent.putExtra("Goal Hours", theGoal.getTime().getHours());
            intent.putExtra("Goal Minutes", theGoal.getTime().getMinutes());
            intent.putExtra("Goal Seconds", theGoal.getTime().getSeconds());
            intent.putExtra("Unproductive Hours", unprouctiveTime.convertMilliToHours(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Minutes", unprouctiveTime.convertMilliToMinutes(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Seconds", unprouctiveTime.convertMilliToSeconds(unprouctiveTime.getMilliseconds()));
            displayed75 = true;
            startActivity(intent);
        }
        else if(settings.isNotifications() && theGoal.getTime().getMilliseconds() <= unprouctiveTime.getMilliseconds() &&
                checkTime.getMilliseconds() != unprouctiveTime.getMilliseconds()){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("Message", "You have used up all of your goal time on unproductive apps!");
            intent.putExtra("Goal Hours", theGoal.getTime().getHours());
            intent.putExtra("Goal Minutes", theGoal.getTime().getMinutes());
            intent.putExtra("Goal Seconds", theGoal.getTime().getSeconds());
            intent.putExtra("Unproductive Hours", unprouctiveTime.convertMilliToHours(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Minutes", unprouctiveTime.convertMilliToMinutes(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Seconds", unprouctiveTime.convertMilliToSeconds(unprouctiveTime.getMilliseconds()));
            startActivity(intent);
        }
    }
}