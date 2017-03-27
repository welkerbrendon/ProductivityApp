package com.example.brendon.productivityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroductionMessageActivity extends AppCompatActivity {
    Button advanceActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_message);
    }

    public void startFirstTimeActivity(View view) {
        Intent intent = new Intent(this, FirstTimeActivity.class);

        startActivity(intent);
    }
}
