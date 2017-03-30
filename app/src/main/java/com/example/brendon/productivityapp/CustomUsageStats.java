package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    public static UsageStatsManager getUsageStatsManager(Context context){

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

    //Getting data usage by date
    public static List<UsageStats> getUsageStatsListByDate(Context context, Calendar date){
        UsageStatsManager usm = getUsageStatsManager(context);

        Calendar startTime = Calendar.getInstance();

        startTime.setTime(date.getTime());
        startTime.add(Calendar.DAY_OF_YEAR, - 1);
        startTime.add(Calendar.SECOND, 1);

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                startTime.getTimeInMillis(), date.getTimeInMillis());

        int debug = 0;
        //Just debugging stuff
        for (int i = 0; i < usageStatsList.size(); i++) {
            debug = 1;
        }

        if (usageStatsList.isEmpty()) {
            Log.d(TAG, "Nothing in usageStatsList");
        }
        else {
            Log.d(TAG, "Loaded queryUsageStats correctly");
        }

        Log.d("Range time", complete.format(startTime.getTimeInMillis()) + " - " +
                complete.format(date.getTimeInMillis()));

        return usageStatsList;
    }

    //Displaying the apps used on list view
    public static void printOnListViewDaily(Context context, ListView listView,
                                            List<UsageStats> usageStatsList){

        List<String> appNames = new ArrayList<String>();

            System.out.println("SIZE: " + usageStatsList.size());

            for (int i = 0; i < usageStatsList.size(); i++)
            {
                if (usageStatsList.get(i).getTotalTimeInForeground() != 0) {

                    String packageName = usageStatsList.get(i).getPackageName();
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
                                + usageStatsList.get(i).getTotalTimeInForeground() / 1000 + "s");
                    }
                }

            }


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appNames);

        listView.setAdapter(itemsAdapter);
    }

    public static void printOnListViewWeekly(Context context, ListView listView,
                                             HashMap<String, Long> usageMap){

        List<String> appNames = new ArrayList<String>();

        System.out.println("SIZE: " + usageMap.size());

        for (HashMap.Entry<String, Long> entry : usageMap.entrySet())
        {
            if (entry.getValue() > 0) {

                String packageName = entry.getKey();
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
                            + entry.getValue() / 1000 + "s");
                }
            }

        }


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appNames);

        listView.setAdapter(itemsAdapter);
    }

}
