package com.example.lbstest.Algorithms;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 天道 北云 on 2017/8/31.
 */

public class AnalyPath {
    private static final String TAG = "AnalyPath";
    public static List<LatLng> getPartCyclePosition(LatLng startPositoin, LatLng endPostion){
        List<LatLng> Cycle=new ArrayList<>();
        double y1=startPositoin.latitude;
        double y2=endPostion.latitude;
        double x1=startPositoin.longitude;
        double x2=endPostion.longitude;
//        double middlePointX=(x1+x2)/2;
//        double middlePointY=(y1+y2)/2;

        double Length=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
        double centerXOfTheCycle=(Math.pow(Length,2)*(Math.sqrt(3)/2*(y1-y2)+1/2*(x1-x2)))/(Math.pow(x1-x2,2)+Math.pow(y1-y2,2))+x2;
        double centerYOfTheCycle=(Math.pow(Length,2)*(1/2*(y1-y2)-Math.sqrt(3)/2*(x1-x2)))/(Math.pow(x1-x2,2)+Math.pow(y1-y2,2))+y2;
        double rOfTheCycle=Length;
        int share=200;

        Cycle.add(startPositoin);
        double primerLength=(x2-x1)/share;
        for(int i=0;i<share;i++){
            Cycle.add(getLongitudeByLatitude(centerXOfTheCycle,centerYOfTheCycle,rOfTheCycle,x1+i*primerLength));
        }
        Cycle.add(new LatLng(centerXOfTheCycle,centerYOfTheCycle));
        return Cycle;
    }
    private static LatLng getLongitudeByLatitude(double centerXOfTheCycle,double centerYOfTheCycle,double rOfTheCycle,double theLongitude){
        return new LatLng(Math.sqrt(Math.pow(rOfTheCycle,2)-Math.pow(theLongitude-centerXOfTheCycle,2))+centerYOfTheCycle,theLongitude);
    }
}

