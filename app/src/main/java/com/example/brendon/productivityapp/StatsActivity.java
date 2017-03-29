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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        //Setting time to 12 PM
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), 1, 0, 0);
        Log.d("Current Time", complete.format(date.getTime()));

        //Setting spinner content
        final CustomSpinner spinner = (CustomSpinner) findViewById(R.id.intervals_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interval_array, android.R.layout.simple_spinner_item);
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

                TextView tv1 = (TextView)findViewById(R.id.textView);
                String formattedDate = daily.format(date.getTime());

                if (tv1 != null) {
                    tv1.setText(formattedDate);
                }

                intervalType = UsageStatsManager.INTERVAL_DAILY;
                updatePieChart(date, intervalType);
                fillListView();

                spinner.setSelection(0);

            }

        };

        //Spinner Listener
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String selectedItem = spinner.getSelectedItem().toString();

                    if (selectedItem.equals("Day View")) {

                        //Changing textview.text
                        TextView tv1 = (TextView)findViewById(R.id.textView);
                        String formattedDate = daily.format(date.getTime());

                        if (tv1 != null) {
                            tv1.setText(formattedDate);
                        }

                        intervalType = UsageStatsManager.INTERVAL_DAILY;
                        updatePieChart(date, intervalType);
                        fillListView();
                    }
                    else if (selectedItem.equals("Week View")) {

                        //Changing textview.text
                        TextView tv1 = (TextView)findViewById(R.id.textView);
                        String formattedDateLast = weekly.format(date.getTime());
                        date.add(Calendar.DAY_OF_YEAR, -7);
                        String formattedDateFirst = weekly.format(date.getTime());
                        date.add(Calendar.DAY_OF_YEAR, 7);

                        if (tv1 != null) {
                            tv1.setText(formattedDateFirst + " - " + formattedDateLast);
                        }

                        intervalType = UsageStatsManager.INTERVAL_WEEKLY;
                        updatePieChart(date, intervalType);
                        fillListView();
                    }
                    else if (selectedItem.equals("Change Date")) {

                        new DatePickerDialog(StatsActivity.this, dateSetListener, date
                                .get(Calendar.YEAR), date.get(Calendar.MONTH),
                                date.get(Calendar.DAY_OF_MONTH)).show();
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
            date.add(Calendar.DAY_OF_YEAR, 7);

            String formattedDateLast = weekly.format(date.getTime());
            date.add(Calendar.DAY_OF_YEAR, -7);
            String formattedDateFirst = weekly.format(date.getTime());
            date.add(Calendar.DAY_OF_YEAR, 7);

            if (tv1 != null) {
                tv1.setText(formattedDateFirst + " - " + formattedDateLast);
            }

            Log.d("WEEKLY INCREASE", formattedDateFirst + " - " + formattedDateLast);
        }

        updatePieChart(date, intervalType);
        fillListView();
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
            date.add(Calendar.DAY_OF_YEAR, -7);

            String formattedDateLast = weekly.format(date.getTime());
            date.add(Calendar.DAY_OF_YEAR, -7);
            String formattedDateFirst = weekly.format(date.getTime());
            date.add(Calendar.DAY_OF_YEAR, 7);

            if (tv1 != null) {
                tv1.setText(formattedDateFirst + " - " + formattedDateLast);
            }

            Log.d("WEEKLY DECREASE", formattedDateFirst + " - " + formattedDateLast);
        }

        updatePieChart(date, intervalType);
        fillListView();
    }

    private void updatePieChart(Calendar date, int intervalType) {
        CustomUsageStats usageStats = new CustomUsageStats();
        List<UsageStats> usageStatsList = usageStats.getUsageStatsListByDate(this, date,
                intervalType);

        PieChart chart = (PieChart) findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();

        if (chart != null) {
            chart.setUsePercentValues(true);
        }

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

        if (chart != null) {
            chart.setData(data);
        }

        if (chart != null) {
            chart.invalidate();
        }
    }

    private void fillListView() {

        ListView listView = (ListView) findViewById(R.id.appList);

        CustomUsageStats.printOnListView(this, listView, date, intervalType);

    }
}
