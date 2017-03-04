package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Frank on 3/1/2017.
 */

public class CustomUsageStats {

    @SuppressWarnings("ResourceType")
    //Creating the userstats manager
    private static UsageStatsManager getUsageStatsManager(Context context){

        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }

    //Getting data usage
    private static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        return usageStatsList;
    }

    //Displaying the apps used on list view
    public void printOnListView(Context context, ListView listView){

        List<UsageStats> statsList = getUsageStatsList(context);

        List<String> appNames = new ArrayList<String>();

        System.out.println("SIZE: " + statsList.size());

        for (int i = 0; i < statsList.size(); i++) {
            if (statsList.get(i).getTotalTimeInForeground() != 0) {
                System.out.println(statsList.get(i).getPackageName());
                appNames.add(statsList.get(i).getPackageName() + " TIME SPENT: "
                        + statsList.get(i).getTotalTimeInForeground() / 100 + "s");
            }
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appNames);
        listView.setAdapter(itemsAdapter);
    }


}
