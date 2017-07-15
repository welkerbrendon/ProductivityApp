package com.example.brendon.productivityapp;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AppOpsManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import android.view.LayoutInflater;

import java.util.List;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;
    private static final int REQUEST_ACCESSIBILITY = 200;
    private static final String MY_ACCESSIBILITY_SERVICE =
            "com.example.brendon.productivityapp/.ProductivityMonitor";
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Fragment fragment = new DashboardFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_view, fragment, "DASH").commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        settings = Settings.getInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (settings.isFirstTime()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        } else {
            if (!Settings.hasUsageAccess(this))
                requestPermission();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager manager = getFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AppSelectorActivity selectorActivity =
                    (AppSelectorActivity) manager.findFragmentByTag("APPS");
            if (selectorActivity != null && selectorActivity.isVisible()) {
                manager.beginTransaction().replace(R.id.content_view, new DashboardFragment(), "DASH").commit();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_snooze) {
            Intent intent = new Intent(getApplicationContext(), SnoozeDialog.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager manager = getFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            DashboardFragment fragment =
                    (DashboardFragment) manager.findFragmentByTag("DASH");
            if (!(fragment != null && fragment.isVisible())) {
                manager.beginTransaction().replace(R.id.content_view, new DashboardFragment(), "DASH").commit();
            }

            //manager.beginTransaction().replace(R.id.content_view, new DashboardFragment(), "DASH").commit();
        } else if (id == R.id.nav_allowance) {
            settings.setAllowance(this);
        } else if (id == R.id.nav_test) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_apps) {
            //toolbar.setTitle("Set Unproductive Apps");
            manager.beginTransaction().replace(R.id.content_view, new AppSelectorActivity(), "APPS").commit();
        } else if (id == R.id.nav_notify) {
            if (Settings.isAccessibilityServiceEnabled(this)) {
                showNotificationSettings();
            } else {
                requestAccessibility();
            }
        } else if (id == R.id.nav_snooze) {
            Intent intent = new Intent(getApplicationContext(), SnoozeDialog.class);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showNotificationSettings() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_settings, null);
        final RadioButton noNotifications = (RadioButton) view.findViewById(R.id.noNotification);
        final RadioButton passiveNotifications = (RadioButton) view.findViewById(R.id.passiveNotification);
        final RadioButton activeNotifications = (RadioButton) view.findViewById(R.id.activeNotification);
        final Button notificationTest = (Button) view.findViewById(R.id.notificationTest);
        final Button notificationConfirm = (Button) view.findViewById(R.id.notificationConfirm);
        final Button notificationCancel = (Button) view.findViewById(R.id.notificationCancel);

        if (settings.isNotifications()) {
            if (settings.getNotificationType() == Notification.PRIORITY_DEFAULT) {
                passiveNotifications.setChecked(true);
            } else if (settings.getNotificationType() == Notification.PRIORITY_HIGH) {
                activeNotifications.setChecked(true);
            }
        } else {
            noNotifications.setChecked(true);
        }

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Notification Settings")
                .setView(view).create();

        notificationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        notificationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noNotifications.isChecked()) {
                    settings.setNotifications(false);
                } else {
                    settings.setNotifications(true);
                    if (passiveNotifications.isChecked()) {
                        settings.setNotificationType(Notification.PRIORITY_DEFAULT);
                    } else if (activeNotifications.isChecked()) {
                        settings.setNotificationType(Notification.PRIORITY_HIGH);
                    }
                }
                dialog.dismiss();
            }
        });
        notificationTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noNotifications.isChecked()) {
                    settings.setNotifications(false);
                    Toast.makeText(getApplicationContext(), "Notifications are disabled", Toast.LENGTH_SHORT).show();
                } else {
                    settings.setNotifications(true);
                    if (passiveNotifications.isChecked()) {
                        settings.setNotificationType(Notification.PRIORITY_DEFAULT);
                    } else if (activeNotifications.isChecked()) {
                        settings.setNotificationType(Notification.PRIORITY_HIGH);
                    }
                    sendDummyNotification();
                }
            }
        });
        dialog.show();
    }

    private void sendDummyNotification() {
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Productivity App Test Notification")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is what your notification will look like"))
                .setPriority(settings.getNotificationType())
                .setVibrate(new long[0]);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_notifications_paused_white_24dp,
                        "Snooze",
                        resultPendingIntent)
                        .build();
        nBuilder.setContentIntent(resultPendingIntent)
                .addAction(snoozeAction)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(6548);
        nm.notify(6548, nBuilder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACCESSIBILITY) {
            showNotificationSettings();
        }
    }

    private void requestAccessibility() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_NoActionBar));
        builder.setMessage("If you want to use this functionality, you will need to " +
                "enable the Productivity App accessibility service.\n\nOn the next screen, click" +
                "Productivity App, turn it on, then accept the confirmation window.")
                .setPositiveButton("Yes, Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(
                                new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS),
                                REQUEST_ACCESSIBILITY);
                    }
                })
                .setNegativeButton("No Thanks", null);
        builder.create().show();
    }
    private void requestPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_NoActionBar));
        builder.setMessage("You must allow the app permission to access Usage Stats.\nIn settings, click Productivity App, then turn on Allow Usage Access.")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(
                                new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS),
                                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                        if (!Settings.isAccessibilityServiceEnabled(getApplicationContext())) {
                            requestAccessibility();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "This app does not work without Usage Stats.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        builder.create().show();
    }
}
