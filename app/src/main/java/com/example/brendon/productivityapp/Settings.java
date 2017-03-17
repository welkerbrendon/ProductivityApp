package com.example.brendon.productivityapp;

/**
 * Created by Scott on 2/22/2017.
 */

public class Settings {
    private boolean notifications;
    private boolean weeklyGoalReminder;
    private boolean dailyPlanReminder;
    private boolean weeklyPlanReminder;
    private boolean lockOut;
    private boolean autoDataSending;
    private boolean takeBreak;
    private boolean firstTime;
    private int timeUntilBreak;
    private int breakLength;
    private int hourForGoalReminder;
    private int minutesForGoalReminder;
    private int hourForDailyPlan;
    private int minutesForDailyPlan;
    private int hourForWeeklyPlan;
    private int minutesForWeeklyPlan;

    Settings() {
        notifications = false;
        weeklyGoalReminder = false;
        dailyPlanReminder = false;
        weeklyPlanReminder = false;
        lockOut = false;
        autoDataSending = false;
        takeBreak = false;
        firstTime = false;
        timeUntilBreak = 0;
        breakLength = 0;
        hourForGoalReminder = 0;
        minutesForGoalReminder = 0;
        hourForDailyPlan = 0;
        minutesForDailyPlan = 0;
        hourForWeeklyPlan = 0;
        minutesForWeeklyPlan = 0;
    }

    public boolean isWeeklyGoalReminder() {
        return weeklyGoalReminder;
    }

    public void setWeeklyGoalReminder(boolean weeklyGoalReminder) {
        this.weeklyGoalReminder = weeklyGoalReminder;
    }

    public boolean isDailyPlanReminder() {
        return dailyPlanReminder;
    }

    public void setDailyPlanReminder(boolean dailyPlanReminder) {
        this.dailyPlanReminder = dailyPlanReminder;
    }

    public boolean isWeeklyPlanReminder() {
        return weeklyPlanReminder;
    }

    public void setWeeklyPlanReminder(boolean weeklyPlanReminder) {
        this.weeklyPlanReminder = weeklyPlanReminder;
    }

    public boolean isLockOut() {
        return lockOut;
    }

    public void setLockOut(boolean lockOut) {
        this.lockOut = lockOut;
    }

    public boolean isAutoDataSending() {
        return autoDataSending;
    }

    public void setAutoDataSending(boolean autoDataSending) {
        this.autoDataSending = autoDataSending;
    }

    public boolean isTakeBreak() {
        return takeBreak;
    }

    public void setTakeBreak(boolean takeBreak) {
        this.takeBreak = takeBreak;
    }

    public int getTimeUntilBreak() {
        return timeUntilBreak;
    }

    public void setTimeUntilBreak(int timeUntilBreak) {
        this.timeUntilBreak = timeUntilBreak;
    }

    public int getBreakLength() {
        return breakLength;
    }

    public void setBreakLength(int breakLength) {
        this.breakLength = breakLength;
    }

    public int getHourForGoalReminder() {
        return hourForGoalReminder;
    }

    public void setHourForGoalReminder(int hourForGoalReminder) {
        this.hourForGoalReminder = hourForGoalReminder;
    }

    public int getMinutesForGoalReminder() {
        return minutesForGoalReminder;
    }

    public void setMinutesForGoalReminder(int minutesForGoalReminder) {
        this.minutesForGoalReminder = minutesForGoalReminder;
    }

    public int getHourForDailyPlan() {
        return hourForDailyPlan;
    }

    public void setHourForDailyPlan(int hourForDailyPlan) {
        this.hourForDailyPlan = hourForDailyPlan;
    }

    public int getMinutesForDailyPlan() {
        return minutesForDailyPlan;
    }

    public void setMinutesForDailyPlan(int minutesForDailyPlan) {
        this.minutesForDailyPlan = minutesForDailyPlan;
    }

    public int getHourForWeeklyPlan() {
        return hourForWeeklyPlan;
    }

    public void setHourForWeeklyPlan(int hourForWeeklyPlan) {
        this.hourForWeeklyPlan = hourForWeeklyPlan;
    }

    public int getMinutesForWeeklyPlan() {
        return minutesForWeeklyPlan;
    }

    public void setMinutesForWeeklyPlan(int minutesForWeeklyPlan) {
        this.minutesForWeeklyPlan = minutesForWeeklyPlan;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
}
