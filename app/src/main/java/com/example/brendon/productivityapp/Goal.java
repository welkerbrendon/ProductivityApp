package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a specific goal a user will use to
 * be more productive with their app usage.
 */

public class Goal {

    public static final String PREFS_NAME = "savedGoal";

    /**
     * This variable enables the user to put personalized
     * information on how the time goal will be achieved.
     */
    String plan;

    /**
     * Determines the specific maximum amount of time the user
     * wants to use on unproductive apps.
     */
    Time time = new Time();
    List<UsageStats> unproductiveApps = new ArrayList<>();


    public Goal(){
        plan = "no plan";
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    /**
     * Saves the plan to *either a text file or SharedPreferences.*
     */
    public void savePlan() {

    }

    public List<UsageStats> getUnproductiveApps() {
        return unproductiveApps;
    }

    public void setUnproductiveApps(ArrayList<UsageStats> unproductiveApps) {
        this.unproductiveApps = unproductiveApps;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    // Saves the goal to shared preferences
    public void saveToSharedPreferences(Context context) {
        // Save goal as a json
        Gson gson = new Gson();
        String json = gson.toJson(this);

        //Save goal in shared preferences
        SharedPreferences settingsPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor settingEditor = settingsPref.edit();
        settingEditor.putString("Goal", json);

        // Commit edits
        settingEditor.commit();
    }
}
