package com.example.brendon.productivityapp;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.NumberPicker;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class is a collection of the different
 * options a user may select to help with
 * accomplishing the goal.
 */

public class Settings {
    public static final String PREFS_NAME = "Settings";
    private static final String MY_ACCESSIBILITY_SERVICE =
            "com.example.brendon.productivityapp/.ProductivityMonitor";

    private static Settings instance;

    private transient SettingsClient client;
    private transient ProductivityMonitor monitor;
    private boolean notifications;
    private boolean weeklyGoalReminder;
    private boolean dailyPlanReminder;
    private boolean weeklyPlanReminder;
    private boolean lockOut;
    private boolean autoDataSending;
    private boolean takeBreak;
    private boolean shownAppTarget;
    private boolean shownSnoozeTarget;
    private boolean firstTime;
    private int notificationType;
    private int timeUntilBreak;
    private long snoozeEndTime;
    private int hourForGoalReminder;
    private int minutesForGoalReminder;
    private int hourForDailyPlan;
    private int minutesForDailyPlan;
    private int hourForWeeklyPlan;
    private int minutesForWeeklyPlan;
    private long timeUsedWeekly;
    private Set<String> unproductiveApps;
    private Map<String, AppSelection> appCache;

    public Map<String, AppSelection> getAppCache() {
        return appCache;
    }

    public void setAppCache(Map<String, AppSelection> appCache) {
        this.appCache = appCache;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public long getTimeRemaining() {
        return getMillisForWeeklyPlan() - timeUsedWeekly;
    }

    public boolean hasShownAppTarget() {
        return shownAppTarget;
    }

    public void setShownAppTarget(boolean shownAppTarget) {
        this.shownAppTarget = shownAppTarget;
    }

    private Settings() {
        notifications = false;
        weeklyGoalReminder = false;
        dailyPlanReminder = false;
        weeklyPlanReminder = false;
        lockOut = false;
        autoDataSending = false;
        takeBreak = false;
        firstTime = true;
        shownAppTarget = false;
        shownSnoozeTarget = false;
        timeUntilBreak = 0;
        snoozeEndTime = 0;
        hourForGoalReminder = 0;
        minutesForGoalReminder = 0;
        hourForDailyPlan = 0;
        minutesForDailyPlan = 0;
        hourForWeeklyPlan = 5;
        minutesForWeeklyPlan = 0;
        timeUsedWeekly = 0;
        unproductiveApps = new HashSet<>();
        appCache = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        notificationType = Notification.PRIORITY_DEFAULT;
    }

    public void setShownSnoozeTarget(boolean shownSnoozeTarget) {
        this.shownSnoozeTarget = shownSnoozeTarget;
    }

    public boolean hasShownSnoozeTarget() {

        return shownSnoozeTarget;
    }

    public static Settings getNewInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public static Settings getInstance(Context context) {
        if (instance == null) {
            Gson gson = new Gson();
            SharedPreferences settingsPref = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String json = settingsPref.getString(PREFS_NAME, "");
            if (!json.equals("")) {
                instance = gson.fromJson(json, Settings.class);
            } else {
                instance = new Settings();
            }
        }
        return instance;
    }

    public long getTimeUsedWeekly() {
        return timeUsedWeekly;
    }

    public void setTimeUsedWeekly(long timeUsedWeekly) {
        this.timeUsedWeekly = timeUsedWeekly;
        if (client != null) {
            client.settingsChanged();
        }
    }

    public List<String> getUnproductiveAppsList() {
        if (unproductiveApps == null) {
            unproductiveApps = new HashSet<>();
            return new ArrayList<>(unproductiveApps);
        } else {
            return new ArrayList<>(unproductiveApps);
        }
    }

    public void setUnproductiveAppsList(Set<String> unproductiveAppsList) {
        this.unproductiveApps = new HashSet<>(unproductiveAppsList);
        if (monitor != null) {
            monitor.reloadAppList();
        }
    }

    public Set<String> getUnproductiveApps() {
        if (unproductiveApps == null) {
            return new HashSet<>();
        } else {
            return new HashSet<>(unproductiveApps);
        }
    }

    public void registerClient(SettingsClient client) {
        this.client = client;
        this.client.settingsChanged();
    }

    public void unregisterClient() {
        client = null;
    }

    public void registerMonitor(ProductivityMonitor monitor) {
        this.monitor = monitor;
    }

    public void unregisterMonitor() {
        monitor = null;
    }

    public boolean isWeeklyGoalReminder() {
        return weeklyGoalReminder;
    }

    public void setWeeklyGoalReminder(boolean weeklyGoalReminder) {
        this.weeklyGoalReminder = weeklyGoalReminder;
    }

    public boolean isDailyPlanReminder() {
        return dailyPlanReminder;
    }

    public void setDailyPlanReminder(boolean dailyPlanReminder) {
        this.dailyPlanReminder = dailyPlanReminder;
    }

    public boolean isWeeklyPlanReminder() {
        return weeklyPlanReminder;
    }

    public void setWeeklyPlanReminder(boolean weeklyPlanReminder) {
        this.weeklyPlanReminder = weeklyPlanReminder;
    }

    public boolean isLockOut() {
        return lockOut;
    }

    public void setLockOut(boolean lockOut) {
        this.lockOut = lockOut;
    }

    public boolean isAutoDataSending() {
        return autoDataSending;
    }

    public void setAutoDataSending(boolean autoDataSending) {
        this.autoDataSending = autoDataSending;
    }

    public boolean isTakeBreak() {
        Calendar c = Calendar.getInstance();
        //Log.d("DBG", Boolean.toString(c.getTimeInMillis() <= snoozeEndTime));
        return (c.getTimeInMillis() <= snoozeEndTime);
    }

    public void setTakeBreak(boolean takeBreak) {
        this.takeBreak = takeBreak;
    }

    public int getTimeUntilBreak() {
        return timeUntilBreak;
    }

    public void setTimeUntilBreak(int timeUntilBreak) {
        this.timeUntilBreak = timeUntilBreak;
    }

    public long getSnoozeEndTime() {
        return snoozeEndTime;
    }

    public void setSnoozeEndTime(long snoozeEndTime) {
        this.snoozeEndTime = snoozeEndTime;
    }

    public int getHourForGoalReminder() {
        return hourForGoalReminder;
    }

    public void setHourForGoalReminder(int hourForGoalReminder) {
        this.hourForGoalReminder = hourForGoalReminder;
    }

    public int getMinutesForGoalReminder() {
        return minutesForGoalReminder;
    }

    public void setMinutesForGoalReminder(int minutesForGoalReminder) {
        this.minutesForGoalReminder = minutesForGoalReminder;
    }

    public int getHourForDailyPlan() {
        return hourForDailyPlan;
    }

    public void setHourForDailyPlan(int hourForDailyPlan) {
        this.hourForDailyPlan = hourForDailyPlan;
    }

    public int getMinutesForDailyPlan() {
        return minutesForDailyPlan;
    }

    public void setMinutesForDailyPlan(int minutesForDailyPlan) {
        this.minutesForDailyPlan = minutesForDailyPlan;
    }

    public int getHourForWeeklyPlan() {
        return hourForWeeklyPlan;
    }

    public void setHourForWeeklyPlan(int hourForWeeklyPlan) {
        this.hourForWeeklyPlan = hourForWeeklyPlan;
        if (client != null) {
            client.settingsChanged();
        }
    }

    public int getMinutesForWeeklyPlan() {
        return minutesForWeeklyPlan;
    }

    public void setMinutesForWeeklyPlan(int minutesForWeeklyPlan) {
        this.minutesForWeeklyPlan = minutesForWeeklyPlan;
        if (client != null) {
            client.settingsChanged();
        }
    }

    public long getMillisForWeeklyPlan() {
        return (hourForWeeklyPlan * 3600000) + (minutesForWeeklyPlan * 60000);
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public void saveToSharedPreferences(Context context) {
        // Save settings as a json
        Gson gson = new Gson();
        String json = gson.toJson(this, Settings.class);

        //Saving settings in shared preferences
        SharedPreferences settingsPref = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor settingEditor = settingsPref.edit();
        settingEditor.putString(PREFS_NAME, json);

        //Commit edits
        settingEditor.apply();
    }

    public void setAllowance(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View spinners = inflater.inflate(R.layout.allowance_setter, null);
        //builder.setView(inflater.inflate(R.layout.allowance_setter, null));
        spinners.post(new Runnable() {
            @Override
            public void run() {
                final NumberPicker npHours = (NumberPicker) spinners.findViewById(R.id.npHours);
                final NumberPicker npMinutes = (NumberPicker) spinners.findViewById(R.id.npMinutes);
                npHours.setMinValue(0);
                npHours.setMaxValue(168);
                npHours.setValue(hourForWeeklyPlan);
                npMinutes.setMinValue(0);
                npMinutes.setMaxValue(59);
                npMinutes.setValue(minutesForWeeklyPlan);
            }
        });
        builder.setView(spinners)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final NumberPicker npHours = (NumberPicker) spinners.findViewById(R.id.npHours);
                        final NumberPicker npMinutes = (NumberPicker) spinners.findViewById(R.id.npMinutes);
                        setHourForWeeklyPlan(npHours.getValue());
                        setMinutesForWeeklyPlan(npMinutes.getValue());
                        saveToSharedPreferences(activity);
                    }
                })
                .setNegativeButton("Cancel", null)
                .setTitle("Set Allowance");
        builder.create().show();
    }

    public static boolean hasUsageAccess(Context context) {
        AppOpsManager appOps = (AppOpsManager)
                context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public static boolean isAccessibilityServiceEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> runningServices = am
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            if (MY_ACCESSIBILITY_SERVICE.equals(service.getId())) {
                return true;
            }
        }
        return false;
    }
}
