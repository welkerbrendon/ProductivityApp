package com.example.brendon.productivityapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.usage.UsageStats;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

interface UsageBuilderResponse {
    void processFinish(CustomAppList builtList);
}

public class DashboardFragment extends Fragment implements UsageBuilderResponse, SettingsClient {

    private Settings settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Productivity App");
        settings = Settings.getInstance(getActivity());
        settings.registerClient(this);
        //settings.copyToHash();
        super.onCreate(savedInstanceState);
        final View v = getView();
        ImageView allowanceSettings = (ImageView) v.findViewById(R.id.allowanceSettings);
        ImageView appSettings = (ImageView) v.findViewById(R.id.appSettings);
        if (allowanceSettings != null)
            allowanceSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settings.setAllowance(getActivity());
                    //Toast.makeText(getApplicationContext(), "Allowance Settings", Toast.LENGTH_SHORT).show();
                }
            });
        if (appSettings != null)
            appSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_view, new AppSelectorActivity(), "APPS").commit();
                }
            });
        VerticalProgressBar progressBar = (VerticalProgressBar) v.findViewById(R.id.verticalProgressBar);
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("This is your progress bar.  As you use unproductive apps, it will fill up.")
                        .setPositiveButton("Got it", null).create().show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        UsageBuilder usageBuilder = new UsageBuilder();
        usageBuilder.delegate = this;
        usageBuilder.execute();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settings.unregisterClient();
    }

    private void updateTimes() {
        final TextView allowance = (TextView) getView().findViewById(R.id.allowance);
        final TextView remaining = (TextView) getView().findViewById(R.id.remaining);
        allowance.setText(String.format(Locale.US,
                "%d:%02d",
                settings.getHourForWeeklyPlan(),
                settings.getMinutesForWeeklyPlan()));
        long timeLeft = settings.getMillisForWeeklyPlan() - settings.getTimeUsedWeekly();
        int mins = 0;
        if (timeLeft < 0) {
            timeLeft *= -1;
            remaining.setTextColor(Color.RED);
            mins = (int) (TimeUnit.MILLISECONDS.toMinutes(timeLeft) % TimeUnit.HOURS.toMinutes(1));
            remaining.setText(String.format(Locale.US,
                    "-%d:%02d",
                    TimeUnit.MILLISECONDS.toHours(timeLeft),
                    mins));
        } else {
            remaining.setTextColor(Color.BLACK);
            mins = (int) (TimeUnit.MILLISECONDS.toMinutes(timeLeft) % TimeUnit.HOURS.toMinutes(1));
            remaining.setText(String.format(Locale.US,
                    "%d:%02d",
                    TimeUnit.MILLISECONDS.toHours(timeLeft),
                    mins));
        }
        VerticalProgressBar verticalProgressBar = (VerticalProgressBar) getView().findViewById(R.id.verticalProgressBar);
        verticalProgressBar.drawUsage();
    }

    @Override
    public void processFinish(CustomAppList builtList) {
        final View v = getView();
        ListView iconListView = (ListView) v.findViewById(R.id.appUsage);
        long usedTime = 0;
        if (builtList != null & iconListView != null)
            iconListView.setAdapter(builtList);
        final List<AppUsageEntry> entries = new ArrayList<>(builtList.getUsageEntries());
        final VerticalProgressBar verticalProgressBar = (VerticalProgressBar) v.findViewById(R.id.verticalProgressBar);
        verticalProgressBar.setUsageEntries(entries);
        for (AppUsageEntry entry : builtList.getUsageEntries()) {
            usedTime += entry.getTimeInForeground();
        }
        settings.setTimeUsedWeekly(usedTime);
        settings.saveToSharedPreferences(getActivity());
        updateTimes();
        Collections.reverse(entries);

        if (!settings.hasShownAppTarget()) {
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(getView().findViewById(R.id.appSettings))
                    .setPrimaryText("Set unproductive apps")
                    .setSecondaryText("You can select which apps you consider unproductive here")
                    .setBackgroundColour(ContextCompat.getColor(getActivity(), R.color.ProductiveGreen))
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                prompt.dismiss();
                            }
                            settings.setShownAppTarget(true);
                        }
                    })
                    .show();
        } else if (!settings.hasShownSnoozeTarget()) {
            AppCompatActivity parent = (AppCompatActivity) getActivity();
            if (parent != null) {
                View snooze = parent.findViewById(R.id.action_snooze);
                if (snooze != null) {
                new MaterialTapTargetPrompt.Builder(this)
                        .setTarget(snooze)
                        .setPrimaryText("Snooze notifications")
                        .setSecondaryText("If you have enabled notifications, you can snooze them here")
                        .setIcon(R.drawable.ic_notifications_paused_white_24dp)
                        .setBackgroundColour(ContextCompat.getColor(getActivity(), R.color.ProductiveGreen))
                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                            @Override
                            public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                    prompt.dismiss();
                                }
                                settings.setShownSnoozeTarget(true);
                            }
                        })
                        .show();
                }
            }
        }
    }

    @Override
    public void settingsChanged() {
        if (getView() != null)
            updateTimes();
    }

    private class UsageBuilder extends AsyncTask<Void, Void, Void> {
        UsageBuilderResponse delegate = null;
        List<UsageStats> usageStatsList;
        CustomAppList builtList;

        @Override
        protected Void doInBackground(Void... voids) {
            builtList = CustomUsageStats.getWeeklyUsage(getActivity());
            return null;
        }

        @Override
        protected void onPreExecute() {
            RelativeLayout usageProgressBar = (RelativeLayout) getView().findViewById(R.id.usageProgressBar);
            if (usageProgressBar != null)
                usageProgressBar.setVisibility(View.VISIBLE);
            //super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RelativeLayout usageProgressBar = (RelativeLayout) getView().findViewById(R.id.usageProgressBar);
            if (usageProgressBar != null)
                usageProgressBar.setVisibility(View.INVISIBLE);
            delegate.processFinish(builtList);
            //super.onPostExecute(aVoid);
        }
    }
}
