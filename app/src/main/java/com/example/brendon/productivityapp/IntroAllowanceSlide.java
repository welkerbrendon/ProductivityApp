package com.example.brendon.productivityapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

public class IntroAllowanceSlide extends Fragment implements IntroSlide {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    public static IntroAllowanceSlide newInstance(int layoutResId) {
        IntroAllowanceSlide introSlide = new IntroAllowanceSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        introSlide.setArguments(args);

        return introSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        View allowanceView = inflater.inflate(R.layout.allowance_setter, null);
        TextView hrs = (TextView) allowanceView.findViewById(R.id.textView3);
        TextView mins = (TextView) allowanceView.findViewById(R.id.textView4);
        NumberPicker npHrs = (NumberPicker) allowanceView.findViewById(R.id.npHours);
        NumberPicker npMins = (NumberPicker) allowanceView.findViewById(R.id.npMinutes);
        setNumberPickerTextColor(npHrs, Color.WHITE);
        setNumberPickerTextColor(npMins, Color.WHITE);
        hrs.setTextColor(Color.WHITE);
        mins.setTextColor(Color.WHITE);
        npHrs.setMinValue(0);
        npHrs.setMaxValue(168);
        npMins.setMinValue(0);
        npMins.setMaxValue(59);
            Settings settings = Settings.getInstance(getActivity());
            npHrs.setValue(settings.getHourForWeeklyPlan());
            npMins.setValue(settings.getMinutesForWeeklyPlan());

        if (view != null) {
            LinearLayout center = (LinearLayout) view.findViewById(R.id.centerPane);
            center.addView(allowanceView);
        } else {
            Log.e("ERR", "View not found");
        }
    }

    private boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException e){
                    Log.w("setNpTextColor", e);
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
