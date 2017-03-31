package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * This Activity represents the First Time a user opens the App.
 * <p>
 *     In this Activity, the User will learn how to use the App
 *     and choose which apps are deemed "unproductive"
 * </p>
 */
public class FirstTimeActivity extends ActionBarActivity implements
        android.widget.CompoundButton.OnCheckedChangeListener{

    public static final String PREFS_NAME = "Unproductive Apps List";

    ListView lv;
    List<AppSelection> appSelectionList = new ArrayList<>();
    List<String> unproductiveApps = new ArrayList<>();
    CustomList appSelectionAdapter;
    Settings settings = new Settings(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        // Get list of all installed apps
        final PackageManager pm = getPackageManager();
        // List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        Context context = getApplicationContext();
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        List<UsageStats> statsList = CustomUsageStats.getUsageStatsList(context);

        // Populate lists of app names and logos
        for (int i = 0; i < statsList.size(); i++) {
            if(statsList.get(i).getTotalTimeInForeground() > 0) {
                ApplicationInfo ai;
                String packageName = statsList.get(i).getPackageName();

                try {
                    ai = pm.getApplicationInfo(packageName, 0);
                }
                catch(final PackageManager.NameNotFoundException e) {
                    ai = null;
                    continue;
                }

                AppSelection appSelection = new AppSelection();

                appSelection.setAppIcon(ai.loadIcon(pm));
                appSelection.setPackageName((String) pm.getApplicationLabel(ai));

                appSelectionList.add(appSelection);
            }
        }

        lv = (ListView) findViewById(R.id.list);
        appSelectionAdapter = new CustomList(appSelectionList, this);
        lv.setAdapter(appSelectionAdapter);

    }

    public List<String> getUnproductiveAppsList() {
        return unproductiveApps;
    }

    public void startEditGoalActivity(View view) {
        Intent intent = new Intent(this, EditGoalActivity.class);

        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int pos = lv.getPositionForView(compoundButton);
        if (pos != ListView.INVALID_POSITION) {
            AppSelection a = appSelectionList.get(pos);
            a.setChecked(b);


            // Add or remove items if appropriate
            if (b == true) {
                if (!unproductiveApps.contains(a.getPackageName())) {
                    unproductiveApps.add(a.getPackageName());
                }
            }
            else {
                if (unproductiveApps.contains(a.getPackageName())) {
                    unproductiveApps.remove(unproductiveApps.indexOf(a.getPackageName()));
                }
            }
        }
    }

    public void onButtonClick(View view) {
        Gson gson = new Gson();
        String json = gson.toJson(unproductiveApps);

        SharedPreferences settingsPref = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor settingEditor = settingsPref.edit();

        settingEditor.putString("Unproductive Apps List", json);

        Toast.makeText(this, "List saved to shared preferences.", Toast.LENGTH_SHORT).show();

        Intent intent;
        if(settings.isFirstTime()){
            intent = new Intent(this, EditGoalActivity.class);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
    }

    public void goNext(View view) {
        Intent intent;

        if(settings.isFirstTime())
            intent = new Intent(this, EditGoalActivity.class);
        else
            intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

}