package com.example.brendon.productivityapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

/**
 * Created by Scott on 3/29/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    public static final int REQUEST_CODE = 12345;
    public static final String EXTRA_GOAL = "GOAL";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "In AlarmReceiver");

        Intent i = new Intent(context, TimeTrackingService.class);
        String jsonGoal = intent.getStringExtra(MainActivity.EXTRA_GOAL);
        i.putExtra(EXTRA_GOAL, jsonGoal);

        context.startService(i);
    }
}
