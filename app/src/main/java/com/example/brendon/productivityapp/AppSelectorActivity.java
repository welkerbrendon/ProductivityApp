package com.example.brendon.productivityapp;

import android.app.Fragment;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This Activity represents the First Time a user opens the App.
 * <p>
 * In this Activity, the User will learn how to use the App
 * and choose which apps are deemed "unproductive"
 * </p>
 */

interface ListBuilderResponse {
    void processFinish(Map<String, AppSelection> builtList);
}



public class AppSelectorActivity extends Fragment implements
        android.widget.CompoundButton.OnCheckedChangeListener,
        ListBuilderResponse {

    public static final String PREFS_SETTINGS_NAME = "Settings";

    ListView lv;
    Map<String, AppSelection> appSelectionList = new TreeMap<>();
    Set<String> unproductiveApps = new HashSet<>();
    CustomList appSelectionAdapter;
    Settings settings;
    Gson gson = new Gson();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_app_selector, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Unproductive Apps");
        settings = Settings.getInstance(getContext());
        unproductiveApps = settings.getUnproductiveApps();
        Button done = (Button) getView().findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setUnproductiveAppsList(unproductiveApps);
                settings.saveToSharedPreferences(getContext());
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_view, new DashboardFragment()).commit();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        AppListBuilder listBuilder = new AppListBuilder();
        listBuilder.delegate = this;
        listBuilder.execute();
    }

    @Override
    public void onDestroy() {
        settings.setUnproductiveAppsList(unproductiveApps);
        settings.saveToSharedPreferences(getContext());
        super.onDestroy();
    }

    public void startEditGoalActivity(View view) {
        Intent intent = new Intent(getContext(), EditGoalActivity.class);

        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int pos = lv.getPositionForView(compoundButton);
        RelativeLayout view = (RelativeLayout) compoundButton.getParent();
        TextView label = (TextView) view.findViewById(R.id.txt);
        if (pos != ListView.INVALID_POSITION) {
            AppSelection a = appSelectionList.get(label.getText().toString());
            a.setChecked(b);

            // Add or remove items if appropriate
            if (b) {
                if (!unproductiveApps.contains(a.getPackageName())) {
                    unproductiveApps.add(a.getPackageName());
                }
            } else {
                if (unproductiveApps.contains(a.getPackageName())) {
                    unproductiveApps.remove(a.getPackageName());
                }
            }
        }
    }

    @Override
    public void processFinish(Map<String, AppSelection> builtList) {
        ProgressBar spinner = (ProgressBar) getView().findViewById(R.id.appListSpinner);
        spinner.setVisibility(View.INVISIBLE);
        View v = getView();
        PackageManager pm = getContext().getPackageManager();
        for (String name : settings.getUnproductiveAppsList()) {
            String appName;
            try {
                appName = pm.getApplicationLabel(pm.getApplicationInfo(name, 0)).toString();
            } catch (PackageManager.NameNotFoundException e) {
                appName = "";
            }
            if (builtList.containsKey(appName)) {
                builtList.get(appName).setChecked(true);
            }
        }
        appSelectionList = builtList;
        lv = (ListView) v.findViewById(R.id.list);
        appSelectionAdapter = new CustomList(new ArrayList<>(builtList.values()),
                getActivity(), this);
        lv.setAdapter(appSelectionAdapter);
    }

    private class AppListBuilder extends AsyncTask<Void, Void, Void> {
        ListBuilderResponse delegate = null;
        private Map<String, AppSelection> appSelectionMap; //=
                //new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        @Override
        protected Void doInBackground(Void... voids) {

            final PackageManager pm = getContext().getPackageManager();

            List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo app : apps) {
                if (pm.getLaunchIntentForPackage(app.packageName) != null) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0 ||
                            (app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        if (appSelectionMap.containsKey(pm.getApplicationLabel(app).toString())) {
                        } else {
                            String packageName = app.packageName;
                            AppSelection appSelection = new AppSelection(packageName, getContext());
                            appSelectionMap.put(appSelection.getAppName(), appSelection);
                        }
                    }
                }
            }
            //settings.setInstalledAppCache(appSelectionMap);
            return null;
        }

        @Override
        protected void onPreExecute() {
            View v = getView();
            if (v != null) {
                ProgressBar spinner = (ProgressBar) getView().findViewById(R.id.appListSpinner);
                if (spinner != null) {
                    spinner.setVisibility(View.VISIBLE);
                }
            }
            appSelectionMap = settings.getAppCache();
            if (appSelectionMap.isEmpty()) {
                appSelectionMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            }
            //super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            settings.setAppCache(appSelectionMap);
            delegate.processFinish(appSelectionMap);
            //super.onPostExecute(o);
        }
    }
}