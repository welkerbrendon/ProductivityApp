package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;

import java.util.ArrayList;

/**
 * This class represents a specific goal a user will use to
 * be more productive with their app usage.
 */

public class Goal {
    /**
     * This variable enables the user to put personalized
     * information on how the time goal will be achieved.
     */
    String plan;

    /**
     * Determines the specific maximum amount of time the user
     * wants to use on unproductive apps.
     */
    Time time;

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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
