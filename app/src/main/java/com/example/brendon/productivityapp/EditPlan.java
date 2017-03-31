package com.example.brendon.productivityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditPlan extends AppCompatActivity {

    public static final String PREFS_NAME = "savedSettings";
    Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        settings = (Settings) getSharedPreferences(PREFS_NAME, 0);

        settings.setFirstTime(false);

        settings.saveToSharedPreferences(this);

        startActivity(intent);
    }
}
