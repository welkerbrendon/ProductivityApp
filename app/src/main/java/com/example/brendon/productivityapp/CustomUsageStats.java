package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class enables the app to gather UsageStats from
 * apps being used on the phone.
 *
 * @author Frank
 */

public class CustomUsageStats {
    private static final String TAG = "CustomUsageStats";
    static SimpleDateFormat complete = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);

    @SuppressWarnings("ResourceType")
    //Creating the userstats manager
    private static UsageStatsManager getUsageStatsManager(Context context){

        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }

    //Getting data usage
    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        // For testing purposes
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime, endTime);
        if (usageStatsList.isEmpty()) {
            Log.d(TAG, "Nothing in usageStatsList");
        }
        else {
            Log.d(TAG, "Loaded queryUsageStats correctly");
        }

        return usageStatsList;
    }

    //Getting data usage
    public static List<UsageStats> getUsageStatsListByDate(Context context, Calendar date, int intervalType){
        UsageStatsManager usm = getUsageStatsManager(context);

        long endTime = date.getTimeInMillis();

        long startTime = date.getTimeInMillis() - 1800000;

        // For testing purposes
        List<UsageStats> usageStatsList = usm.queryUsageStats(intervalType,
                startTime, endTime);
        if (usageStatsList.isEmpty()) {
            Log.d(TAG, "Nothing in usageStatsList");
        }
        else {
            Log.d(TAG, "Loaded queryUsageStats correctly");
        }

        Log.d("Range time", complete.format(startTime) + " - " + complete.format(endTime));
        return usageStatsList;
    }

    //Displaying the apps used on list view
    public static void printOnListView(Context context, ListView listView, Calendar date,
                                       int intervalType){

        List<UsageStats> statsList = getUsageStatsListByDate(context, date, intervalType);

        List<String> appNames = new ArrayList<String>();

        System.out.println("SIZE: " + statsList.size());

        for (int i = 0; i < statsList.size(); i++) {
            if (statsList.get(i).getTotalTimeInForeground() != 0) {

                String packageName = statsList.get(i).getPackageName();
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo ai;
                try {
                    ai = packageManager.getApplicationInfo(packageName, 0);
                }
                catch(final PackageManager.NameNotFoundException e) {
                    ai = null;
                }

                if (ai != null) {
                    String appName = (String) packageManager.getApplicationLabel(ai);

                    System.out.println(appName);
                    appNames.add(appName + " TIME SPENT: "
                            + statsList.get(i).getTotalTimeInForeground() / 1000 + "s");
                }
            }
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appNames);

        listView.setAdapter(itemsAdapter);
    }


}
