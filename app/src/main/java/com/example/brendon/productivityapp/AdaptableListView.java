package com.example.brendon.productivityapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Frank on 3/28/2017.
 */

public class AdaptableListView extends ListView {


        public AdaptableListView  (Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public AdaptableListView  (Context context) {
            super(context);
        }

        public AdaptableListView  (Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }

}
