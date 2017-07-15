package com.example.brendon.productivityapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;

public class IntroUsageAccessSlide extends Fragment implements IntroSlide {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    public static IntroUsageAccessSlide newInstance(int layoutResId) {
        IntroUsageAccessSlide introSlide = new IntroUsageAccessSlide();

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
    public void onResume() {
        super.onResume();
        GifView gif = (GifView) getView().findViewById(R.id.image);
        gif.play();
        testSettingsAreActive(getView());
    }

    @Override
    public void onPause() {
        GifView gif = (GifView) getView().findViewById(R.id.image);
        gif.pause();
        super.onPause();
    }

    private void testSettingsAreActive(View v) {
        if (Settings.hasUsageAccess(getActivity())) {
            TextView message = (TextView) v.findViewById(R.id.description);
            message.setText("We already have usage access!  Awesome!");
            GifView gif = (GifView) v.findViewById(R.id.image);
            gif.pause();
            gif.setVisibility(View.INVISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutResId, container, false);
        testSettingsAreActive(v);
        return v;
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
