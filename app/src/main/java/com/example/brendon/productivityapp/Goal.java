package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;

import java.util.ArrayList;

/**
 * Created by Scott on 2/22/2017.
 */

public class Goal {
    String plan;
    Time time;
    ArrayList<UsageStats> unproductiveApps; // Here, we shall store our list of unproductive Apps.

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void savePlan() {

    }

    public ArrayList<UsageStats> getUnproductiveApps() {
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
}
