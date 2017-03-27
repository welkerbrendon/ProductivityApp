package com.example.brendon.productivityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        List<String> strings = new ArrayList<>();

        for (int i = 0; i < 20; i++)
            strings.add("Number " + i);

        CustomListCheckText adapter = new CustomListCheckText(SettingsActivity.this, strings);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void startEditPlan(View view) {
        Intent intent = new Intent(this, EditPlan.class);

        startActivity(intent);
    }

}
