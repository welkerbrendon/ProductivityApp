package com.example.brendon.productivityapp;

import android.app.DatePickerDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

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

                            new DatePickerDialog(StatsActivity.this, dateSetListener, date
                                    .get(Calendar.YEAR), date.get(Calendar.MONTH),
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

        if (intervalType == UsageStatsManager.INTERVAL_DAILY) {

            date.add(Calendar.DAY_OF_YEAR, 1);
            String formattedDate = daily.format(date.getTime());

            if (tv1 != null) {
                tv1.setText(formattedDate);
            }

            Log.d("DAILY INCREASE", formattedDate);
        }

        if (intervalType == UsageStatsManager.INTERVAL_WEEKLY) {

            date.add(Calendar.WEEK_OF_YEAR, 1);
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

        Log.d("Interval Type", String.valueOf(intervalType));

        if (intervalType == UsageStatsManager.INTERVAL_DAILY) {
            List<UsageStats> usageStatsList = CustomUsageStats.getUsageStatsListByDate(this, date);

            for (int i = 0; i < usageStatsList.size(); i++) {
                if (usageStatsList.get(i).getTotalTimeInForeground() != 0) {
                    entries.add(new PieEntry(usageStatsList.get(i).getTotalTimeInForeground(),
                            usageStatsList.get(i).getPackageName()));
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
                if (entry.getValue() > 0)
                entries.add(new PieEntry(entry.getValue(),
                        entry.getKey()));
            }

            fillListViewWeekly(usageStatsHashMap);
        }


        PieDataSet set = new PieDataSet(entries, "Productivity");

        //Formating stuff
        if (chart != null) {
            chart.setUsePercentValues(true);
        }
        //Setting colors

        set.setColors(new int[] {R.color.colorPrimaryDark, R.color.colorAccent}, this);
        PieData data = new PieData(set);

        if (chart != null) {
            chart.setData(data);
        }

        if (chart != null) {
            chart.invalidate();
        }
    }

    private void updateLineChart(Calendar date) {

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
                }
            }

            startTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        LineChart chart = (LineChart) findViewById(R.id.lineChart);
        List<Entry> entries = new ArrayList<>();

        int day = 1;

        for (HashMap.Entry<String, Long> entry : usageStatsHashMap.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new Entry(day, entry.getValue()));
            }
            day++;
        }

        if (!entries.isEmpty()) {
            LineDataSet set = new LineDataSet(entries, "Productivity");

            //Setting colors

            set.setColors(new int[]{R.color.colorPrimaryDark, R.color.colorAccent}, this);
            LineData data = new LineData(set);
            data.setValueTextSize(20);

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

        ListView listView = (ListView) findViewById(R.id.appList);

        CustomUsageStats.printOnListViewDaily(this, listView, usageStatsList);

    }

    private void fillListViewWeekly(HashMap<String, Long> usageMap) {

        ListView listView = (ListView) findViewById(R.id.appList);

        CustomUsageStats.printOnListViewWeekly(this, listView, usageMap);

    }
}
