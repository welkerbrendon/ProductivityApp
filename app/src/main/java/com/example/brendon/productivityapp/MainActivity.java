package com.example.brendon.productivityapp;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "savedSettings";
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;
    //public static final String USAGE_STATS_SERVICE = "usagestats";
    //SharedPreferences settingsPref = getSharedPreferences(PREFS_NAME, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user has enabled Usage Stats. If not, request it
        if (!hasPermission())
            requestPermission();

        //Loading settings, commented out because doesn't work yet
        /*Gson gson = new Gson();
        String json = settingsPref.getString("Settings", "");
        Settings settings = gson.fromJson(json, Settings.class);*/

        //Time tracking

        ListView listView = (ListView)findViewById(R.id.listView);
        CustomUsageStats usageStats = new CustomUsageStats();
        usageStats.printOnListView(this, listView);
    }

    protected void onPause() {
        super.onPause();

        //Saving settings, commented out because doesn't work yet
        /*
        Settings settings = new Settings();

        SharedPreferences.Editor settingEditor = settingsPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        settingEditor.putString("Settings", json);

        settingEditor.commit();*/
    }

    // Checks if the user has granted permission to the app
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestPermission() {
        Toast.makeText(getApplicationContext(),
                "You must allow the app permission to access Usage Stats./nOpening System Settings.",
                Toast.LENGTH_SHORT).show();

        startActivityForResult(
                new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
    }
}
