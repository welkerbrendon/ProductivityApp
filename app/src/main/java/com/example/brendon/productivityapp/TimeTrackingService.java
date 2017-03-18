package com.example.brendon.productivityapp;

import android.app.IntentService;
import android.app.usage.UsageStats;
import android.content.Intent;

import java.util.ArrayList;

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

    public TimeTrackingService() {
        super("TimeTrackingService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Call calculate method to start calculation of unproductive time spent
        //calculateUnproductiveTimeSpent(workIntent.getParcelableArrayListExtra());
    }

    public void calculateUnproductiveTimeSpent(ArrayList<UsageStats> unproductiveAppsList) {

    }

}
