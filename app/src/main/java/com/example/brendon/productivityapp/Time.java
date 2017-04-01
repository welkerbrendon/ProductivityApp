package com.example.brendon.productivityapp;

/**
 * This class allows the app to convert between human-readable
 * time and app-readable time.
 */

public class Time {
    public int hours;
    public int minutes;
    public int seconds;
    public long milliseconds;

    Time() {hours = 0; minutes = 0; seconds = 0; milliseconds = 0;}

    // Getters and Setters
    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    // Business Functions
    public long convertHoursToMilli(int hours) {
        return 3600000 * hours;
    }

    public long convertMinutesToMilli(int minutes) {
        return 60000 * minutes;
    }

    public long convertSecondsToMilli(int seconds) { return 1000 * seconds; }

    public int convertMilliToHours(long milliseconds) { return (int) milliseconds/3600000;}

    public int convertMilliToMinutes(long milliseconds) {
        return (int) ((milliseconds - convertMilliToHours(milliseconds))/60000);
    }

    public int convertMilliToSeconds(long milliseconds) {
        return (int) ((milliseconds - convertMilliToHours(milliseconds) -
                convertMilliToMinutes(milliseconds))/60);
    }
}
