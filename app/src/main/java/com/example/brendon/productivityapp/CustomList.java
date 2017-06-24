package com.example.brendon.productivityapp;

/**
 * Created by Tyler on 3/13/2017.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class AppSelection implements Runnable {
    private transient PackageManager pm;
    private String packageName;
    private String appName;
    private transient boolean checked = false;
    private transient Drawable appIcon;
    //public void setPackageName(String appName) {
    //    this.appName = appName;
    //}

    AppSelection() {

    }

    AppSelection(String packageName, Context context) {
        this.packageName = packageName;
        pm = context.getPackageManager();
        try {
            //appIcon = pm.getApplicationIcon(appName);
            appName = (String) pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            //appInfo = new ApplicationInfo();
            //appIcon = pm.getDefaultActivityIcon();
            appName = packageName;
        }
        if (pm != null) {
            AsyncTask.execute(this);
        }
    }

    //public ApplicationInfo getAppInfo() {
//        return appInfo;
    //  }

    //public void setAppInfo(ApplicationInfo appInfo) {
    //  this.appInfo = appInfo;
    //}

    public void loadIconAsync(PackageManager pm) {
        this.pm = pm;
        AsyncTask.execute(this);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
        //return (String) context.getPackageManager().getApplicationLabel(appInfo);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return getPackageName();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AppSelection && packageName.equals(((AppSelection) o).getPackageName());
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public void run() {
        if (pm != null) {
            try {
                appIcon = pm.getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                appIcon = pm.getDefaultActivityIcon();
            }
        } else {
        }
    }
}

public class CustomList extends ArrayAdapter<AppSelection> {


    private List<AppSelection> appSelectionList;
    private Context context;
    private CompoundButton.OnCheckedChangeListener listener;

    public CustomList(List<AppSelection> appSelectionList, Context context,
                      CompoundButton.OnCheckedChangeListener listener) {
        super(context, R.layout.app_selector_entry, appSelectionList);
        this.appSelectionList = appSelectionList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final PackageManager pm = context.getPackageManager();
        AppSelectionHolder holder = new AppSelectionHolder();

        if (v == null || v.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.app_selector_entry, null);

            holder.appName = (TextView) v.findViewById(R.id.txt);
            holder.appIcon = (ImageView) v.findViewById(R.id.img);
            holder.chkBox = (CheckBox) v.findViewById(R.id.checkBox);
            holder.packageName = (TextView) v.findViewById(R.id.pkgName);

            holder.chkBox.setOnCheckedChangeListener(listener);
        } else {
            holder = (AppSelectionHolder) v.getTag();
        }

        AppSelection a = appSelectionList.get(position);
        holder.appName.setText(a.getAppName());
        if (position < 11) {
            if (a.getAppIcon() == null)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("THREAD", "Interrupted");
                }
        }
        holder.appIcon.setImageDrawable(a.getAppIcon());
        holder.chkBox.setChecked(a.isChecked());
        holder.chkBox.setTag(a);
        holder.packageName.setText(a.getPackageName());

        return v;
    }

    private static class AppSelectionHolder {
        TextView appName;
        ImageView appIcon;
        CheckBox chkBox;
        TextView packageName;
    }
}

