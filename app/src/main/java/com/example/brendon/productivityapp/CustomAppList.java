package com.example.brendon.productivityapp;
/**
 * Created by Frank on 4/1/2017.
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

public class CustomAppList extends ArrayAdapter<String>{

    private final Activity context;
    private final List<String> text;
    private final List<Drawable> image;
    public CustomAppList(Activity context,
                      List<String> web, List<Drawable> imageId) {
        super(context, R.layout.custom_list_view, web);
        this.context = context;
        this.text = web;
        this.image = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list_view, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt2);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img2);
        txtTitle.setText(text.get(position));

        imageView.setImageDrawable(image.get(position));
        return rowView;
    }
}