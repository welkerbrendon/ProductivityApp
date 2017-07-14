package com.example.brendon.productivityapp;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FragmentHost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_host);
        int resId = getIntent().getIntExtra("RES_ID", 0);
        if (resId == R.layout.fragment_app_selector) {
            FragmentManager fm = getFragmentManager();
            AppSelectorActivity selectorActivity = new AppSelectorActivity();
            Bundle bundle = new Bundle();
            bundle.putBoolean("IS_FIRST_RUN", true);
            selectorActivity.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment_container, selectorActivity, "FRAGMENT").commit();
        }
    }
}
