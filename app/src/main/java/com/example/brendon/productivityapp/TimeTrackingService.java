package com.example.brendon.productivityapp;

import android.app.IntentService;
import android.app.usage.UsageStats;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Scott on 3/13/2017.
 */

public class TimeTrackingService extends IntentService {

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
    private Settings settings;

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

    public void calculateUnproductiveTimeSpent(ArrayList<UsageStats> unproductiveAppsList) {
        Time checkTime;
        checkTime = unprouctiveTime;
        // Update unproductiveTime

        // Create CustomUsageStats Object
        // Call getUsageStatsList
        // Compare the NAMES (or PackageNames) to SharedPreferences List of Unproductive Apps
        // For all apps that match the name, get their timeInMilli and += to unproductiveTime.

        // With the new unproductiveTime variable, it is now ready to be checked against
        //  notifications(checkTime);



        notifications(checkTime);
    }

    public void notifications(Time checkTime){
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
