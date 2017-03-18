package com.example.brendon.productivityapp;

import android.app.IntentService;
import android.app.usage.UsageStats;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * This service will run in the background when started.
 * <p>
 *     This service calculates the amount of time
 *     from the time it was last called to the
 *     time it is re-instantiated that apps
 *     have been in the foreground.
 * </p>
 */

public class TimeTrackingService extends IntentService {
    private static final String PREFS_NAME = "UnproductiveAppsFile";
    private static final String JSON_LIST_KEY = "key";

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

    private boolean displayed25;
    private boolean displayed50;
    private boolean displayed75;
    private Goal theGoal;
    private Time unprouctiveTime;

    public TimeTrackingService(Goal userGoal) {
        super("TimeTrackingService");
        displayed25 = false;
        displayed50 = false;
        displayed75 = false;
        theGoal = userGoal;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Call calculate method to start calculation of unproductive time spent
        //calculateUnproductiveTimeSpent(workIntent.getParcelableArrayListExtra());
    }

    public void calculateUnproductiveTimeSpent() {
        Time checkTime;
        checkTime = unprouctiveTime;
        // Update unproductiveTime

        // Create CustomUsageStats Object
        CustomUsageStats usageStatsAccessor = new CustomUsageStats();

        // Call getUsageStatsList
        List<UsageStats> allAppsList = usageStatsAccessor.getUsageStatsList(this);

        // Compare the NAMES (or PackageNames) to SharedPreferences List of Unproductive Apps
        SharedPreferences appsPreferences = getSharedPreferences(PREFS_NAME, 0);

        // We will retrieve the unproductiveAppsList by JSON
        String jsonList = appsPreferences.getString(JSON_LIST_KEY, null);
        List<UsageStats> unproductiveAppsList = new Gson().fromJson(jsonList,
                new TypeToken<List<UsageStats>>() {
                }.getType());

        // For all apps that match the name, get their timeInMilli and += to unproductiveTime.


        // With the new unproductiveTime variable, it is now ready to be checked against
        //  notifications(checkTime);



        notifications(checkTime);
    }

    public void notifications(Time checkTime){
        if(!displayed25 && theGoal.getTime().getMilliseconds()*.25 <= unprouctiveTime.getMilliseconds()) {
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
        else if(!displayed50 && theGoal.getTime().getMilliseconds()*.5 <= unprouctiveTime.getMilliseconds()){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("Message", "You have used up at least 50% of your goal time on unproductive apps.");
            intent.putExtra("Goal Hours", theGoal.getTime().getHours());
            intent.putExtra("Goal Minutes", theGoal.getTime().getMinutes());
            intent.putExtra("Goal Seconds", theGoal.getTime().getSeconds());
            intent.putExtra("Unproductive Hours", unprouctiveTime.convertMilliToHours(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Minutes", unprouctiveTime.convertMilliToMinutes(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Seconds", unprouctiveTime.convertMilliToSeconds(unprouctiveTime.getMilliseconds()));
            startActivity(intent);
        }
        else if(!displayed75 && theGoal.getTime().getMilliseconds()*.75 <= unprouctiveTime.getMilliseconds()){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("Message", "You have used up at least 75% of your goal time on unproductive apps!");
            intent.putExtra("Goal Hours", theGoal.getTime().getHours());
            intent.putExtra("Goal Minutes", theGoal.getTime().getMinutes());
            intent.putExtra("Goal Seconds", theGoal.getTime().getSeconds());
            intent.putExtra("Unproductive Hours", unprouctiveTime.convertMilliToHours(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Minutes", unprouctiveTime.convertMilliToMinutes(unprouctiveTime.getMilliseconds()));
            intent.putExtra("Unproductive Seconds", unprouctiveTime.convertMilliToSeconds(unprouctiveTime.getMilliseconds()));
            startActivity(intent);
        }
        else if(theGoal.getTime().getMilliseconds() <= unprouctiveTime.getMilliseconds() &&
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
