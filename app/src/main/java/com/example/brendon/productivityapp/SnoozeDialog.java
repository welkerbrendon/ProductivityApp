package com.example.brendon.productivityapp;

import android.content.DialogInterface;
import java.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class SnoozeDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Settings settings = Settings.getInstance(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_snooze_dialog, null);
        final NumberPicker np = (NumberPicker) view.findViewById(R.id.snoozeNP);
        final RadioButton minutes = (RadioButton) view.findViewById(R.id.snoozeMinutes);
        final RadioButton hours = (RadioButton) view.findViewById(R.id.snoozeHours);
        final AppCompatActivity activity = this;
        view.post(new Runnable() {
            @Override
            public void run() {
                np.setMinValue(0);
                np.setMaxValue(59);
            }
        });
        builder.setTitle("Snooze time")
                .setView(view)
                .setPositiveButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar c = Calendar.getInstance();
                        if (minutes.isChecked()) {
                            settings.setSnoozeEndTime(c.getTimeInMillis() +
                                    (np.getValue() * 60 * 1000));
                        } else if (hours.isChecked()) {
                            settings.setSnoozeEndTime(c.getTimeInMillis() +
                                    (np.getValue() * 60 * 60 * 1000));
                        }
                        activity.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        activity.finish();
                    }
                });
        builder.create().show();
        //setContentView(R.layout.activity_snooze_dialog);
    }
}
