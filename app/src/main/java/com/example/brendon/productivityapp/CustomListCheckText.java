package com.example.brendon.productivityapp;

/**
 * Created by Tyler on 3/24/2017.
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

public class CustomListCheckText extends ArrayAdapter<String>{

    private final Activity context;
    private final List<String> settingsList;

    public CustomListCheckText(Activity context, List<String> settingsList) {
        super(context, R.layout.list_single, settingsList);
        this.context = context;
        this.settingsList = settingsList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_check_text, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        txtTitle.setText(settingsList.get(position));

        return rowView;
    }

}
