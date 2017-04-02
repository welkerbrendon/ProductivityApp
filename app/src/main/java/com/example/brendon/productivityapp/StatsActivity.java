package com.example.brendon.productivityapp;

import android.app.DatePickerDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatsActivity extends AppCompatActivity {

    private Calendar date;
    private int intervalType;
    static SimpleDateFormat daily = new SimpleDateFormat("EEE, d MMM", Locale.US);
    static SimpleDateFormat weekly = new SimpleDateFormat("MMMM dd", Locale.US);
    static SimpleDateFormat complete = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        date = Calendar.getInstance();
        //Setting time to 5:59:59 PM
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), 17, 59, 59);
        Log.d("Current Time", complete.format(date.getTime()));

        //Setting spinner content
        final CustomSpinner spinner = (CustomSpinner) findViewById(R.id.intervals_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interval_array1, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        //DatePicker Listener
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                intervalType = UsageStatsManager.INTERVAL_DAILY;

                TextView tv1 = (TextView)findViewById(R.id.textView);
                String formattedDate = daily.format(date.getTime());

                if (tv1 != null) {
                    tv1.setText(formattedDate);
                }

                updatePieChart(date);

                if (spinner != null) {
                    spinner.setSelection(0);
                }

            }

        };

        //Spinner Listener
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String selectedItem = spinner.getSelectedItem().toString();

                    switch (selectedItem) {
                        case "Day View": {

                            //Changing textview.text
                            TextView tv1 = (TextView) findViewById(R.id.textView);
                            String formattedDate = daily.format(date.getTime());

                            if (tv1 != null) {
                                tv1.setText(formattedDate);
                            }

                            intervalType = UsageStatsManager.INTERVAL_DAILY;
                            updatePieChart(date);
                            break;
                        }
                        case "Week View": {

                            //Changing textview.text
                            TextView tv1 = (TextView) findViewById(R.id.textView);
                            String formattedDateLast = weekly.format(date.getTime());
                            date.add(Calendar.DAY_OF_YEAR, -7);
                            String formattedDateFirst = weekly.format(date.getTime());
                            date.add(Calendar.DAY_OF_YEAR, 7);

                            if (tv1 != null) {
                                tv1.setText(formattedDateFirst + " - " + formattedDateLast);
                            }

                            intervalType = UsageStatsManager.INTERVAL_WEEKLY;
                            updatePieChart(date);
                            break;
                        }
                        case "Change Date":

                            new DatePickerDialog(StatsActivity.this, dateSetListener,
                                    date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                                    date.get(Calendar.DAY_OF_MONTH)).show();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
        }

    }

    public void advanceDate(View view) {

        TextView tv1 = (TextView) findViewById(R.id.textView);
        Calendar currentDate = Calendar.getInstance();

        if (intervalType == UsageStatsManager.INTERVAL_DAILY) {

            if (currentDate.get(Calendar.DAY_OF_YEAR) > date.get(Calendar.DAY_OF_YEAR)) {
                date.add(Calendar.DAY_OF_YEAR, 1);
                String formattedDate = daily.format(date.getTime());

                if (tv1 != null) {
                    tv1.setText(formattedDate);
                }

                Log.d("DAILY INCREASE", formattedDate);
            }
        }

        if (intervalType == UsageStatsManager.INTERVAL_WEEKLY) {

            date.add(Calendar.WEEK_OF_YEAR, 1);

            if (date.get(Calendar.DAY_OF_YEAR) > currentDate.get(Calendar.DAY_OF_YEAR)) {
                date.add(Calendar.WEEK_OF_YEAR, -1);
                date.set(Calendar.DAY_OF_YEAR,  currentDate.get(Calendar.DAY_OF_YEAR));
            }
            String formattedDateLast = weekly.format(date.getTime());
            date.add(Calendar.WEEK_OF_YEAR, -1);
            String formattedDateFirst = weekly.format(date.getTime());
            date.add(Calendar.WEEK_OF_YEAR, 1);

            if (tv1 != null) {
                tv1.setText(formattedDateFirst + " - " + formattedDateLast);
            }

            Log.d("WEEKLY INCREASE", formattedDateFirst + " - " + formattedDateLast);
        }

        updatePieChart(date);
        updateLineChart(date);
    }

    public void decreaseDate(View view) {

        TextView tv1 = (TextView) findViewById(R.id.textView);

        if (intervalType == UsageStatsManager.INTERVAL_DAILY) {
            date.add(Calendar.DAY_OF_YEAR, -1);

            String formattedDate = daily.format(date.getTime());

            if (tv1 != null) {
                tv1.setText(formattedDate);
            }

            Log.d("DAILY DECREASE", formattedDate);
        }
        else if (intervalType == UsageStatsManager.INTERVAL_WEEKLY) {

            date.add(Calendar.WEEK_OF_YEAR, -1);
            String formattedDateLast = weekly.format(date.getTime());
            date.add(Calendar.WEEK_OF_YEAR, -1);
            String formattedDateFirst = weekly.format(date.getTime());
            date.add(Calendar.WEEK_OF_YEAR, 1);

            if (tv1 != null) {
                tv1.setText(formattedDateFirst + " - " + formattedDateLast);
            }

            Log.d("WEEKLY DECREASE", formattedDateFirst + " - " + formattedDateLast);
        }

        updatePieChart(date);
        updateLineChart(date);
    }

    public void showLineChart(View view) {

        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        if (lineChart != null) {
            lineChart.setVisibility(View.VISIBLE);
        }
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        if (pieChart != null) {
            pieChart.setVisibility(View.INVISIBLE);
        }

        updateLineChart(date);

        CustomSpinner spinner = (CustomSpinner) findViewById(R.id.intervals_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interval_array2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
    }

    public void showPieChart(View view) {

        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        if (lineChart != null) {
            lineChart.setVisibility(View.INVISIBLE);
        }
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        if (pieChart != null) {
            pieChart.setVisibility(View.VISIBLE);
        }

        updatePieChart(date);

        CustomSpinner spinner = (CustomSpinner) findViewById(R.id.intervals_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interval_array1, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

    }

    private void updatePieChart(Calendar date) {

        List<PieEntry> entries = new ArrayList<>();
        PieChart chart = (PieChart) findViewById(R.id.pieChart);

        String packageName;
        PackageManager packageManager = this.getPackageManager();
        ApplicationInfo ai;

        Log.d("Interval Type", String.valueOf(intervalType));

        if (intervalType == UsageStatsManager.INTERVAL_DAILY) {
            List<UsageStats> usageStatsList = CustomUsageStats.getUsageStatsListByDate(this, date);

            for (int i = 0; i < usageStatsList.size(); i++) {
                packageName = usageStatsList.get(i).getPackageName();

                try {
                    ai = packageManager.getApplicationInfo(packageName, 0);
                }
                catch(final PackageManager.NameNotFoundException e) {
                    ai = null;
                }

                if (usageStatsList.get(i).getTotalTimeInForeground() != 0 && ai != null) {
                    entries.add(new PieEntry(usageStatsList.get(i).getTotalTimeInForeground(),
                            (String) packageManager.getApplicationLabel(ai)));
                }
            }

            fillListViewDaily(usageStatsList);
        }
        else if (intervalType == UsageStatsManager.INTERVAL_WEEKLY) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(date.getTime());
            startTime.add(Calendar.DAY_OF_YEAR, - 6);

            Log.d("Range time weekly", complete.format(startTime.getTimeInMillis()) + " - " +
                    complete.format(date.getTimeInMillis()));

            List<UsageStats> usageStatsList;

            HashMap<String, Long> usageStatsHashMap = new HashMap<>();

            for (int j = 1; j <= 7; j++) {
                usageStatsList = CustomUsageStats.getUsageStatsListByDate(this, startTime);

                for (int i = 0; i < usageStatsList.size(); i++){
                    if (usageStatsList.get(i).getTotalTimeInForeground() > 0) {
                        if (!usageStatsHashMap.containsKey(usageStatsList.get(i).getPackageName())) {
                            usageStatsHashMap.put(usageStatsList.get(i).getPackageName(),
                                    usageStatsList.get(i).getTotalTimeInForeground());
                        }
                        else {
                            usageStatsHashMap.put(usageStatsList.get(i).getPackageName(),
                                    usageStatsList.get(i).getTotalTimeInForeground() +
                                    usageStatsHashMap.get(usageStatsList.get(i).getPackageName()));
                        }
                    }
                    ;
                }

                startTime.add(Calendar.DAY_OF_YEAR, 1);
            }



            for (HashMap.Entry<String, Long> entry : usageStatsHashMap.entrySet())
            {
                packageName = entry.getKey();

                try {
                    ai = packageManager.getApplicationInfo(packageName, 0);
                }
                catch(final PackageManager.NameNotFoundException e) {
                    ai = null;
                }

                if (entry.getValue() > 0) {
                    entries.add(new PieEntry(entry.getValue(),
                            (String) packageManager.getApplicationLabel(ai)));
                }
            }

            fillListViewWeekly(usageStatsHashMap);
        }

        PieDataSet set = new PieDataSet(entries, "Apps Used");

        //Formating stuff
        if (chart != null) {
            chart.setUsePercentValues(true);
        }
        //Setting colors
        set.setColors(this.getResources().getIntArray(R.array.rainbow));
        set.setValueTextSize(13);
        set.setValueTextColor(R.color.black);
        set.setValueLineColor(R.color.black);

        //Setting radius
        chart.setHoleRadius(0);
        chart.setTransparentCircleRadius(0);

        Description description = new Description();
        description.setText("Apps used");
        description.setTextSize(15);
        chart.setDescription(description);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        PieData data = new PieData();
        data.addDataSet(set);

        data.setValueTextSize(20);
        data.setValueTextColor(R.color.black);
        data.setValueFormatter(new PercentFormatter());

        chart.setData(data);
        chart.invalidate();

    }

    private void updateLineChart(Calendar date) {

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(date.getTime());
        startTime.add(Calendar.DAY_OF_YEAR, - 6);

        PackageManager packageManager = this.getPackageManager();
        String packageName;
        ApplicationInfo ai;

        Log.d("Range time weekly", complete.format(startTime.getTimeInMillis()) + " - " +
                complete.format(date.getTimeInMillis()));

        List<UsageStats> usageStatsList;

        HashMap<String, Long> usageStatsHashMap = new HashMap<>();

        int totallyRandomNumber = 1;

        for (int j = 1; j <= 7; j++) {
            usageStatsList = CustomUsageStats.getUsageStatsListByDate(this, startTime);

            for (int i = 0; i < usageStatsList.size(); i++){
                if (usageStatsList.get(i).getTotalTimeInForeground() > 0) {
                    if (!usageStatsHashMap.containsKey(usageStatsList.get(i).getPackageName())) {
                        usageStatsHashMap.put(usageStatsList.get(i).getPackageName(),
                                usageStatsList.get(i).getTotalTimeInForeground());
                    }
                    else {
                        usageStatsHashMap.put(usageStatsList.get(i).getPackageName(),
                                usageStatsList.get(i).getTotalTimeInForeground() +
                                        usageStatsHashMap.get(usageStatsList.get(i).getPackageName()));
                    }
                }
            }

            startTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        LineChart chart = (LineChart) findViewById(R.id.lineChart);
        List<Entry> entries = new ArrayList<>();

        for (HashMap.Entry<String, Long> entry : usageStatsHashMap.entrySet()) {

            if (entry.getValue() > 0) {
                entries.add(new Entry(totallyRandomNumber, entry.getValue() / 60000));
            }
            totallyRandomNumber++;
        }

        if (!entries.isEmpty()) {
            LineDataSet set = new LineDataSet(entries, "Productivity");

            //Setting colors

            set.setColors(this.getResources().getIntArray(R.array.rainbow));
            set.setLineWidth(3);
            LineData data = new LineData(set);
            data.setValueTextSize(20);

            Legend legend = chart.getLegend();
            legend.setEnabled(false);

            Description description = new Description();
            description.setText("Minutes");

            if (chart != null) {
                chart.setData(data);
            }


            if (chart != null) {
                chart.invalidate();
            }
        }
        // I just added this line because there was a bug with github, so I have to pretend
        // I'm coding
        else {
            LineData data = new LineData();

            if (chart != null) {
                chart.setData(data);
            }


            if (chart != null) {
                chart.invalidate();
            }
        }
    }

    private void fillListViewDaily(List<UsageStats> usageStatsList) {

        ListView iconListView = (ListView) findViewById(R.id.app_list_view);
        CustomUsageStats.printOnListViewDaily(StatsActivity.this ,this, iconListView, usageStatsList);

    }

    private void fillListViewWeekly(HashMap<String, Long> usageMap) {

        ListView iconListView = (ListView) findViewById(R.id.app_list_view);

        CustomUsageStats.printOnListViewWeekly(StatsActivity.this ,this, iconListView, usageMap);

    }
}
