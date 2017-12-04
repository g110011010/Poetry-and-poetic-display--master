package com.example.lbstest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by 天道 北云 on 2017/9/18.
 */

public class MapVenueDisplay extends LinearLayout {
    public MapVenueDisplay(Context context,AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.map_venue_display,this);
    }
}
