package com.example.brendon.productivityapp;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * This class enables the app to gather UsageStats from
 * apps being used on the phone.
 *
 * @author Frank
 */

class AppUsageEntry {
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getTimeInForeground() {
        return timeInForeground;
    }

    public void setTimeInForeground(long timeInForeground) {
        this.timeInForeground = timeInForeground;
    }

    public void addTimeInForeground(long timeInForeground) {
        this.timeInForeground += timeInForeground;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String toString() {
        return appName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AppUsageEntry) {
            return appName == ((AppUsageEntry) o).appName;
        }
        return false;
        //return super.equals(o);
    }

    private String appName;
    private long timeInForeground;
    private Drawable icon;
}

class CustomUsageStats {
    private static final String TAG = "CustomUsageStats";
    static SimpleDateFormat complete = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
    private static final String PREFS_NAME = "appList";
    private static final String PREFS_SETTINGS_NAME = "Settings";

    @SuppressWarnings("ResourceType")
    //Creating the userstats manager
    public static UsageStatsManager getUsageStatsManager(Context context) {

        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }

    //Getting data usage
    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        // For testing purposes
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime, endTime);
        if (usageStatsList.isEmpty()) {
        } else {
        }

        return usageStatsList;
    }

    //Getting data usage by date
    public static List<UsageStats> getUsageStatsListByDate(Context context, Calendar date) {
        UsageStatsManager usm = getUsageStatsManager(context);

        Calendar startTime = Calendar.getInstance();

        startTime.setTime(date.getTime());
        startTime.add(Calendar.DAY_OF_YEAR, -1);
        startTime.add(Calendar.SECOND, 1);

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                startTime.getTimeInMillis(), date.getTimeInMillis());

        return usageStatsList;
    }

    public static CustomAppList getWeeklyUsage(Context context) {
        return printOnListViewDaily(context,
                getUsageStatsListThisWeek(context));
    }

    public static List<UsageStats> getUsageStatsListThisWeek(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);

        Calendar cal = Calendar.getInstance();
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        long startTime = cal.getTimeInMillis();

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                startTime, endTime);

        int debug = 0;
        //Just debugging stuff
        for (int i = 0; i < usageStatsList.size(); i++) {
            debug = 1;
        }

        return usageStatsList;
    }

    //Displaying the apps used on list view
    public static CustomAppList printOnListViewDaily(Context context,
                                                     List<UsageStats> usageStatsList) {

        List<AppUsageEntry> listViewEntries = new ArrayList<>();
        Drawable icon = new ColorDrawable(Color.TRANSPARENT);
        Set<String> usagePackageNames = new HashSet<>();
        for (UsageStats entry : usageStatsList) {
            usagePackageNames.add(entry.getPackageName());
        }

        Set<String> unproductiveApps = Settings.getInstance(context).getUnproductiveApps();

        PackageManager packageManager = context.getPackageManager();
        for (String packageName : unproductiveApps) {
            if (!usagePackageNames.contains(packageName)) {
                try {
                    icon = packageManager.getApplicationIcon(packageName);
                    icon = convertToGrayscale(icon);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                ApplicationInfo ai;
                try {
                    ai = packageManager.getApplicationInfo(packageName, 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }

                if (ai != null) {
                    String appName = (String) packageManager.getApplicationLabel(ai);
                    AppUsageEntry entry = new AppUsageEntry();
                    entry.setAppName(appName);

                    entry.setTimeInForeground(0);
                    entry.setIcon(icon);
                    listViewEntries.add(entry);
                }
            }
        }
        for (int i = 0; i < usageStatsList.size(); i++) {
            String packageName = usageStatsList.get(i).getPackageName();
            if (unproductiveApps.contains(packageName)) {
                if (packageManager.getLaunchIntentForPackage(packageName) != null) {
                    //if ((usageStatsList.get(i).getTotalTimeInForeground() / 60000) != 0) {

                        try {
                            icon = packageManager.getApplicationIcon(packageName);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        ApplicationInfo ai;
                        try {
                            ai = packageManager.getApplicationInfo(packageName, 0);
                        } catch (final PackageManager.NameNotFoundException e) {
                            ai = null;
                        }

                        if (ai != null) {
                            String appName = (String) packageManager.getApplicationLabel(ai);
                            AppUsageEntry entry = new AppUsageEntry();
                            //System.out.println(appName);
                            entry.setAppName(appName);
                            if (listViewEntries.contains(entry)) {
                                listViewEntries.get(listViewEntries.indexOf(entry))
                                        .addTimeInForeground(usageStatsList.get(i).getTotalTimeInForeground());
                                continue;
                            }
                            entry.setTimeInForeground(usageStatsList.get(i).getTotalTimeInForeground());
                            if (entry.getTimeInForeground() == 0) {
                                icon = convertToGrayscale(icon);
                            }
                            entry.setIcon(icon);
                            listViewEntries.add(entry);
                        }
                    //}
                }
            }
        }
        Collections.sort(listViewEntries, new Comparator<AppUsageEntry>() {
            @Override
            public int compare(AppUsageEntry o1, AppUsageEntry o2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getAppName(), o2.getAppName());
                return (res != 0) ? res : o1.getAppName().compareTo(o2.getAppName());
            }
        });
        Collections.sort(listViewEntries, new Comparator<AppUsageEntry>() {
            @Override
            public int compare(AppUsageEntry appUsageEntry, AppUsageEntry t1) {
                return (int) (t1.getTimeInForeground() - appUsageEntry.getTimeInForeground());
            }
        });
        //Collections.reverse(listViewEntries);
        return new CustomAppList(context, listViewEntries);
    }

    private static Drawable convertToGrayscale(Drawable drawable)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }
}

    /*public static void printOnListViewWeekly(Activity activity, Context context, ListView iconListView,
                                             HashMap<String, Long> usageMap){

        // Array of strings for ListView Title
        //List<String> listviewTitle = new ArrayList<String>();
        //List<String> listViewTime = new ArrayList<>();
        //List<Drawable> listviewImage = new ArrayList<Drawable>();
        List<AppUsageEntry> listViewEntries = new ArrayList<>();
        Drawable icon = new ColorDrawable(Color.TRANSPARENT);;
        System.out.println("SIZE: " + usageMap.size());

        int i = 0;
        for (HashMap.Entry<String, Long> entry : usageMap.entrySet())
        {
            if (entry.getValue() > 0) {

                String appName = entry.getKey();
                PackageManager packageManager = context.getPackageManager();
                try {
                    icon = packageManager.getApplicationIcon(appName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                ApplicationInfo ai;
                try {
                    ai = packageManager.getApplicationInfo(appName, 0);
                }
                catch(final PackageManager.NameNotFoundException e) {
                    ai = null;
                }

                if (ai != null) {
                    String appName = (String) packageManager.getApplicationLabel(ai);
                    AppUsageEntry appEntry = new AppUsageEntry();
                    //System.out.println(appName);
                    appEntry.setAppName(appName);
                    appEntry.setTimeInForeground(entry.getValue());
                    appEntry.setIcon(icon);
                    listViewEntries.add(appEntry);
                }
                ++i;
            }

        }

        CustomAppList adapterCustom = new CustomAppList(activity, listViewEntries);

        iconListView.setAdapter(adapterCustom);
    }

}
*/