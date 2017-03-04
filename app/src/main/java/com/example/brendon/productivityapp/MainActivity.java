package com.example.brendon.productivityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "savedSettings";
    //public static final String USAGE_STATS_SERVICE = "usagestats";
    //SharedPreferences settingsPref = getSharedPreferences(PREFS_NAME, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loading settings, commented out because doesn't work yet
        /*Gson gson = new Gson();
        String json = settingsPref.getString("Settings", "");
        Settings settings = gson.fromJson(json, Settings.class);*/

        //Time tracking

        ListView listView = (ListView)findViewById(R.id.listView);
        CustomUsageStats usageStats = new CustomUsageStats();

        // Here, we call the reading of the apps function

        usageStats.printOnListView(this, listView);
    }

    protected void onPause() {
        super.onPause();

        //Saving settings, commented out because doesn't work yet
        /*
        Settings settings = new Settings();

        SharedPreferences.Editor settingEditor = settingsPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        settingEditor.putString("Settings", json);

        settingEditor.commit();*/
    }

}
