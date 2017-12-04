package com.example.lbstest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by BeiYun on 2017/8/21.
 */

public class MarkerInfo extends LinearLayout {
    public MarkerInfo(Context context, AttributeSet attrs){
        super(context,attrs);
            LayoutInflater.from(context).inflate(R.layout.map_marker_inf,this);

    }
}
