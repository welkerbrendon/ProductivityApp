package com.example.brendon.productivityapp;

/**
 * Created by Tyler on 3/13/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewGroupCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class AppSelection {
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    String packageName;
    boolean checked = false;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    Drawable appIcon;

}

public class CustomList extends ArrayAdapter<AppSelection>{


    private List<AppSelection> appSelectionList;
    private Context context;

    public CustomList(List<AppSelection> appSelectionList, Context context) {
        super(context, R.layout.list_single, appSelectionList);
        this.appSelectionList = appSelectionList;
        this.context = context;
    }

    private static class AppSelectionHolder {
        public TextView packageName;
        public ImageView appIcon;
        public CheckBox chkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        AppSelectionHolder holder = new AppSelectionHolder();

        if (v == null || v.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_single, null);

            holder.packageName = (TextView) v.findViewById(R.id.txt);
            holder.appIcon = (ImageView) v.findViewById(R.id.img);
            holder.chkBox = (CheckBox) v.findViewById(R.id.checkBox);

            holder.chkBox.setOnCheckedChangeListener((FirstTimeActivity) context);
        }
        else {
            holder = (AppSelectionHolder) v.getTag();
        }

        AppSelection a = appSelectionList.get(position);
        holder.packageName.setText(a.getPackageName());
        holder.appIcon.setImageDrawable(a.getAppIcon());
        holder.chkBox.setChecked(a.isChecked());
        holder.chkBox.setTag(a);

        return v;
    }
}

