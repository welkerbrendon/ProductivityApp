package com.example.brendon.productivityapp;
/**
 * Created by Frank on 4/1/2017.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CustomAppList extends ArrayAdapter<AppUsageEntry>{

    private final Context context;
    private List<AppUsageEntry> _usageEntries = new ArrayList<>();
    public CustomAppList(Context context,
                      List<AppUsageEntry> usageEntries) {
        super(context, R.layout.app_usage_view, usageEntries);
        this.context = context;
        this._usageEntries = usageEntries;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView= inflater.inflate(R.layout.app_usage_view, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt2);
        TextView timeTitle = (TextView) rowView.findViewById(R.id.txt3);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img2);
        txtTitle.setText(_usageEntries.get(position).getAppName());
        //Time time = new Time();
        long millis = _usageEntries.get(position).getTimeInForeground();
        if (millis != 0) {
            String hms = String.format(Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
            timeTitle.setText("Time spent: " + hms);
        } else {
            timeTitle.setText(R.string.not_used);
        }

        imageView.setImageDrawable(_usageEntries.get(position).getIcon());
        return rowView;
    }
    public List<AppUsageEntry> getUsageEntries() { return _usageEntries; };
}