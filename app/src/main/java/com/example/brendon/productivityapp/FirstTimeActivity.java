package com.example.brendon.productivityapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * This Activity represents the First Time a user opens the App.
 * <p>
 *     In this Activity, the User will learn how to use the App
 *     and choose which apps are deemed "unproductive"
 * </p>
 */
public class FirstTimeActivity extends Activity {
    List<String> appNames = new ArrayList<>();
    List<Drawable> appLogos = new ArrayList<>();

    /**
     * This function is instantiated when the Activity is created.
     * <p>
     *     This function makes a list of the apps with their icons installed on the phone
     *     and displays them to the screen.
     * </p>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        // Get list of all installed apps
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Populate lists of app names and logos
        for (int i = 0; i < apps.size(); i++) {
            appNames.add((String) pm.getApplicationLabel(apps.get(i)));
            appLogos.add(apps.get(i).loadIcon(pm));
        }


        CustomList adapter = new CustomList(FirstTimeActivity.this, appNames, appLogos);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

}