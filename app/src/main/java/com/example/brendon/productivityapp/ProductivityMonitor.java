package com.example.brendon.productivityapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by mikah on 06/22/2017.
 */

public class ProductivityMonitor extends AccessibilityService {
    Settings settings;
    private String lastPackage = "";
    private NotificationManager nm;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (settings.isNotifications()) {
            if (!settings.isTakeBreak()) {
                String packageName = event.getPackageName().toString();
                updateUsedTime();
                if (!event.getPackageName().toString().equals("com.android.systemui") &&
                        !lastPackage.equals(packageName)) {
                    // Ignore system, this shows up with notifications and app switcher
                    nm.cancel(6548);
                    PackageManager pm = getPackageManager();
                    lastPackage = event.getPackageName().toString();
                    if (settings.getUnproductiveApps().contains(packageName)) {
                        int mins = (int) (TimeUnit.MILLISECONDS.toMinutes(settings.getTimeUsedWeekly())
                                % TimeUnit.HOURS.toMinutes(1));
                        String timeUsed = String.format(Locale.US,
                                "%d:%02d",
                                TimeUnit.MILLISECONDS.toHours(settings.getTimeUsedWeekly()),
                                mins);
                        String timeGoal = String.format(Locale.US,
                                "%d:%02d",
                                settings.getHourForWeeklyPlan(),
                                settings.getMinutesForWeeklyPlan());
                        String appName;
                        try {
                            appName = (String) pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
                        } catch (PackageManager.NameNotFoundException e) {
                            appName = "";
                        }
                        // Send notification
                        String message = appName + " has been marked as unproductive.  " +
                                "You have used " + timeUsed + " of your " + timeGoal + " goal.";

                        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                                .setContentTitle("Unproductive app warning")
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setPriority(settings.getNotificationType())
                                .setVibrate(new long[0]);
                        Intent resultIntent = new Intent(this, SnoozeDialog.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                        stackBuilder.addParentStack(SnoozeDialog.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Action snoozeAction =
                                new NotificationCompat.Action.Builder(
                                        R.drawable.ic_notifications_paused_white_24dp,
                                        "Snooze",
                                        resultPendingIntent)
                                        .build();
                        resultIntent = new Intent(this, Main3Activity.class);
                        stackBuilder = TaskStackBuilder.create(this);
                        stackBuilder.addParentStack(Main3Activity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        resultPendingIntent = stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        nBuilder.setContentIntent(resultPendingIntent)
                                .addAction(snoozeAction)
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        nm.notify(6548, nBuilder.build());
                    }
                }
            }
        }
    }

    private void updateUsedTime() {
        long usedTime = 0;
        CustomAppList builtList = CustomUsageStats.getWeeklyUsage(getApplicationContext());
        for (AppUsageEntry entry : builtList.getUsageEntries()) {
            usedTime += entry.getTimeInForeground();
        }
        settings.setTimeUsedWeekly(usedTime);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = getServiceInfo();
        settings = Settings.getInstance(this);
        settings.registerMonitor(this);
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settings.unregisterMonitor();
    }

    public void reloadAppList() {

    }

    @Override
    public void onInterrupt() {

    }
}
