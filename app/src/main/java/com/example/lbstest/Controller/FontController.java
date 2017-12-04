package com.example.lbstest.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.example.lbstest.R;

/**
 * 此类用于对字体进行控制
 * Created by BeiYun on 2017/9/2.
 * @author BeiYun
 */

public class FontController{
    /**
     * 此函数为工具函数用于将某TextView组件的字体设置为中文行楷。
     * @param context 接收当前的活动实例。
     * @param textView 需要将字体修改为行楷的TextView对象。
     */
    public static void setTheRunningAndRegularForTextView(Context context, TextView textView){
        Typeface typeFace =Typeface.createFromAsset(context.getAssets(),"fonts/runningAndRegular.TTF");
        textView.setTypeface(typeFace);
    }
}
