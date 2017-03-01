package com.example.brendon.productivityapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "savedSettings";
    SharedPreferences settingsPref = getSharedPreferences(PREFS_NAME, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Gson gson = new Gson();
        String json = settingsPref.getString("Settings", "");
        Settings settings = gson.fromJson(json, Settings.class);
    }

    protected void onPause() {
        super.onPause();

        Settings settings = new Settings();

        SharedPreferences.Editor settingEditor = settingsPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        settingEditor.putString("Settings", json);

        settingEditor.commit();
    }
}
