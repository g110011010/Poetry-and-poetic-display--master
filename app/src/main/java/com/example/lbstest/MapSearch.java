package com.example.lbstest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by BeiYun on 2017/8/22.
 */

public class MapSearch extends LinearLayout {
    public MapSearch(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.map_search,this);
    }
}
