package com.example.brendon.productivityapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.NumberPicker;

import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(IntroBasicSlide.newInstance(R.layout.fragment_intro));
        addSlide(IntroAllowanceSlide.newInstance(R.layout.fragment_intro_allowance));
        addSlide(IntroAppSelectSlide.newInstance(R.layout.fragment_intro_app_selection));
        addSlide(IntroBasicSlide.newInstance(R.layout.fragment_intro_progressbar));
        addSlide(IntroUsageAccessSlide.newInstance(R.layout.fragment_intro_usage_access));
        addSlide(IntroNotificationsSlide.newInstance(R.layout.fragment_intro_notifications));
        addSlide(IntroBasicSlide.newInstance(R.layout.fragment_intro_end));
        showSkipButton(false);
        setImageNextButton(null);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if (oldFragment instanceof IntroSlide && newFragment instanceof IntroSlide){
            // Request Usage Access
            if (((IntroSlide) oldFragment).getLayoutResId() == R.layout.fragment_intro_usage_access &&
                    ((IntroSlide) newFragment).getLayoutResId() == R.layout.fragment_intro_notifications) {
                if (!Settings.hasUsageAccess(this)) {
                    startActivityForResult(
                            new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS),
                            MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                }
            // Request Accessibility Service
            } else if (((IntroSlide) oldFragment).getLayoutResId() == R.layout.fragment_intro_notifications &&
                    ((IntroSlide) newFragment).getLayoutResId() == R.layout.fragment_intro_end) {
                CheckBox notificationOptOut = (CheckBox) oldFragment.getView().findViewById(R.id.checkBox3);
                if (!notificationOptOut.isChecked() && !Settings.isAccessibilityServiceEnabled(this)) {
                    startActivityForResult(
                            new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS),
                            MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                }
            } else if (((IntroSlide) newFragment).getLayoutResId() == R.layout.fragment_intro_end) {
                setProgressButtonEnabled(true);
            }
        }
        if (oldFragment instanceof IntroAllowanceSlide) {
            Settings settings = Settings.getInstance(this);
            View v = oldFragment.getView();
            if (v != null) {
                NumberPicker npHrs = (NumberPicker) v.findViewById(R.id.npHours);
                NumberPicker npMins = (NumberPicker) v.findViewById(R.id.npMinutes);
                if (npHrs != null && npMins != null) {
                    settings.setMinutesForWeeklyPlan(npMins.getValue());
                    settings.setHourForWeeklyPlan(npHrs.getValue());
                }
            }
        }
        if (newFragment instanceof IntroSlide) {
            if (((IntroSlide) newFragment).getLayoutResId() == R.layout.fragment_intro_progressbar) {
                View v = newFragment.getView();
                if (v != null) {
                    VerticalProgressBar vpb = (VerticalProgressBar) v.findViewById(R.id.verticalProgressBar);
                    if (vpb != null) {
                        vpb.drawDemo();
                    }
                }
            }
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Settings.getInstance(this).setFirstTime(false);
        finish();
    }
}
