package com.example.brendon.productivityapp;

/**
 * Created by Tyler on 3/13/2017.
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final List<String> appNames;
    private final List<Drawable> appLogos;
    public CustomList(Activity context,
                      List<String> appNames, List<Drawable> appLogos) {
        super(context, R.layout.list_single, appNames);
        this.context = context;
        this.appNames = appNames;
        this.appLogos = appLogos;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(appNames.get(position));

        imageView.setImageDrawable(appLogos.get(position));
        return rowView;
    }
}