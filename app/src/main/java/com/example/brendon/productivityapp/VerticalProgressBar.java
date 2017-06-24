package com.example.brendon.productivityapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mchapman on 6/13/17.
 */

public class VerticalProgressBar extends RelativeLayout {
    private List<AppUsageEntry> usageEntries = new ArrayList<>();
    private Settings settings;

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            settings = Settings.getInstance(context);
        }
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.usage_bar, this);
    }

    public void setUsageEntries(List<AppUsageEntry> entries) {
        this.usageEntries = new ArrayList<>(entries);
        Collections.reverse(usageEntries);
        drawUsage();
    }

    public void drawUsage() {
        final LinearLayout usageItems = (LinearLayout) findViewById(R.id.usageItems);
        usageItems.removeAllViews();
        ImageView gradient = new ImageView(getContext());
        gradient.setImageResource(R.drawable.gradient_horizontal_invert);
        gradient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())));
        usageItems.addView(gradient);
        gradient.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
        for (AppUsageEntry entry : usageEntries) {
            addUsageElement(entry, settings.getMillisForWeeklyPlan());
        }
    }

    public void addUsageElement(AppUsageEntry entry, long goal) {
        final LinearLayout usageItems = (LinearLayout) findViewById(R.id.usageItems);
        View newItem = new View(getContext());
        Bitmap icon = drawableToBitmap(entry.getIcon());
        Palette palette = Palette.from(icon).generate();
        int color = palette.getVibrantColor(Color.BLACK);
        int border = changeLightness(color, (float) -0.1);
        long timeUsed = entry.getTimeInForeground();
        float factor = ((float)timeUsed / (float)goal);
        int itemHeight = (int) (factor * usageItems.getHeight());
        newItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)itemHeight));
        //newItem.setBackground(getResources().getDrawable(R.drawable.border, null));
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(palette.getVibrantColor(Color.BLACK));
        gd.setStroke(3, border);
        //newItem.setBackgroundColor(palette.getVibrantColor(Color.BLACK));
        newItem.setBackground(gd);
        usageItems.addView(newItem);
        newItem.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    private int changeLightness(int color, float amount) {
        float hsv[] = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv);
        hsv[2] = hsv[2] + amount;
        return Color.HSVToColor(0xff, hsv);
    }
}
