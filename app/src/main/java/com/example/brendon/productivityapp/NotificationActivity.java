package com.example.brendon.productivityapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Brendon on 3/17/2017.
 */

public class NotificationActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();
        TextView notification = (TextView) findViewById(R.id.NotificationMessage);
        String message = intent.getStringExtra("Message");
        notification.setText(message);

        TextView GoalTime = (TextView) findViewById(R.id.GoalTime);
        int hours = intent.getIntExtra("Goal Hours", 0);
        int minutes = intent.getIntExtra("Goal Minutes", 0);
        int seconds = intent.getIntExtra("Goal Seconds", 0);
        GoalTime.setText(hours + ":" + minutes + ":" + seconds);

        TextView UnproTime = (TextView) findViewById(R.id.unproductiveTime);
        int hours2 = intent.getIntExtra("Unproductive Hours", 0);
        int minutes2 = intent.getIntExtra("Unproductive Minutes", 0);
        int seconds2 = intent.getIntExtra("Unproductive Seconds", 0);
        UnproTime.setText(hours2 + ":" + minutes2 + ":" + seconds2);
    }
}
