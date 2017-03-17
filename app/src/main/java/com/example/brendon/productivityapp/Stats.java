package com.example.brendon.productivityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class Stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        float unproductive = 4;
        float productive = 6;

        PieChart chart = (PieChart) findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();

        chart.setUsePercentValues(true);

        entries.add(new PieEntry(unproductive, "Unproductive"));
        entries.add(new PieEntry(productive, "Productive"));

        PieDataSet set = new PieDataSet(entries, "Productivity");

        //Setting colors

        set.setColors(new int[] {R.color.colorPrimaryDark, R.color.colorAccent}, this);
        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }
}
