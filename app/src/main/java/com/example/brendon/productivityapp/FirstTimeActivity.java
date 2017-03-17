package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class FirstTimeActivity extends Activity {
    List<String> appNames = new ArrayList<>();
    List<Drawable> appLogos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        // Get list of all installed apps
        final PackageManager pm = getPackageManager();
        // List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        Context context = getApplicationContext();
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        List<UsageStats> statsList = CustomUsageStats.getUsageStatsList(context);

        // Populate lists of app names and logos
        for (int i = 0; i < statsList.size(); i++) {
            if(statsList.get(i).getTotalTimeInForeground() > 0) {
                ApplicationInfo ai;
                String packageName = statsList.get(i).getPackageName();

                try {
                    ai = pm.getApplicationInfo(packageName, 0);
                }
                catch(final PackageManager.NameNotFoundException e) {
                    ai = null;
                    continue;
                }

                appNames.add((String) pm.getApplicationLabel(ai));
                appLogos.add(ai.loadIcon(pm));
            }
        }

        CustomList adapter = new
                CustomList(FirstTimeActivity.this, appNames, appLogos);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

}