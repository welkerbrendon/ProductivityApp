package com.example.brendon.productivityapp;

import android.app.usage.UsageStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        date = Calendar.getInstance();

        CustomUsageStats usageStats = new CustomUsageStats();
        List<UsageStats> usageStatsList = usageStats.getUsageStatsListByDate(this, date.getTime());

        float unproductive = 4;
        float productive = 6;

        PieChart chart = (PieChart) findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();

        chart.setUsePercentValues(true);

        for (int i = 0; i < usageStatsList.size(); i++) {
            if (usageStatsList.get(i).getTotalTimeInForeground() != 0) {
                entries.add(new PieEntry(usageStatsList.get(i).getTotalTimeInForeground(),
                        usageStatsList.get(i).getPackageName()));
            }

        }

        PieDataSet set = new PieDataSet(entries, "Productivity");

        //Setting colors

        set.setColors(new int[] {R.color.colorPrimaryDark, R.color.colorAccent}, this);
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();

        Spinner spinner = (Spinner) findViewById(R.id.intervals_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interval_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }

    public void advanceDate(View view) {

        date.add(Calendar.DAY_OF_YEAR, 1);
        TextView tv1 = (TextView)findViewById(R.id.textView);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date.getTime());

        tv1.setText(formattedDate);
        Log.d("I DON'T NEED A TAG", formattedDate);

        CustomUsageStats usageStats = new CustomUsageStats();
        List<UsageStats> usageStatsList = usageStats.getUsageStatsListByDate(this, date.getTime());

        PieChart chart = (PieChart) findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();

        chart.setUsePercentValues(true);

        for (int i = 0; i < usageStatsList.size(); i++) {
            if (usageStatsList.get(i).getTotalTimeInForeground() != 0) {
                entries.add(new PieEntry(usageStatsList.get(i).getTotalTimeInForeground(),
                        usageStatsList.get(i).getPackageName()));
            }

        }

        PieDataSet set = new PieDataSet(entries, "Productivity");

        //Setting colors

        set.setColors(new int[] {R.color.colorPrimaryDark, R.color.colorAccent}, this);
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }

    public void decreaseDate(View view) {

        date.add(Calendar.DAY_OF_YEAR, -1);
        TextView tv1 = (TextView)findViewById(R.id.textView);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date.getTime());

        tv1.setText(formattedDate);
        Log.d("I DON'T NEED A TAG", formattedDate);

        CustomUsageStats usageStats = new CustomUsageStats();
        List<UsageStats> usageStatsList = usageStats.getUsageStatsListByDate(this, date.getTime());

        PieChart chart = (PieChart) findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();

        chart.setUsePercentValues(true);

        for (int i = 0; i < usageStatsList.size(); i++) {
            if (usageStatsList.get(i).getTotalTimeInForeground() != 0) {
                entries.add(new PieEntry(usageStatsList.get(i).getTotalTimeInForeground(),
                        usageStatsList.get(i).getPackageName()));
            }

        }

        PieDataSet set = new PieDataSet(entries, "Productivity");

        //Setting colors

        set.setColors(new int[] {R.color.colorPrimaryDark, R.color.colorAccent}, this);
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }
}
