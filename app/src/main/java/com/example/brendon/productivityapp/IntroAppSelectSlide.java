package com.example.brendon.productivityapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class IntroAppSelectSlide extends Fragment implements IntroSlide {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    public static IntroAppSelectSlide newInstance(int layoutResId) {
        IntroAppSelectSlide introSlide = new IntroAppSelectSlide();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutResId, container, false);
        Button selectNow = (Button) v.findViewById(R.id.appSelectButton);
        selectNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FragmentHost.class);
                intent.putExtra("RES_ID", R.layout.fragment_app_selector);
                startActivity(intent);
            }
        });
        return v;
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
