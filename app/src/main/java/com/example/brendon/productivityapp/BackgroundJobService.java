package com.example.brendon.productivityapp;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Scott on 3/29/2017.
 */

public class BackgroundJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent intent = new Intent(this, TimeTrackingService.class);

        startService(intent);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}