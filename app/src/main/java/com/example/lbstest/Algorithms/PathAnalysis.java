package com.example.lbstest.Algorithms;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 天道 北云 on 2017/8/31.
 */

public class PathAnalysis {
    private static final String TAG = "PathAnalysis";
    private static int share=2;
    public static List<LatLng> getPreviousPartCyclePosition(LatLng startPositoin, LatLng endPostion){
        List<LatLng> previousCycle=new ArrayList<>();
        double y1=startPositoin.latitude;
        double y2=endPostion.latitude;
        double x1=startPositoin.longitude;
        double x2=endPostion.longitude;
        double middlePointX=(x1+x2)/2;
        double middlePointY=(y1+y2)/2;
        double heightDifference=(y2-y1)/6/share;
        double widthDifference=(x2-x1)/2/share;
        for(int i=0;i<share;i++){
            previousCycle.add(new LatLng(y1+i*heightDifference,x1+i*widthDifference));
        }
        return previousCycle;
    }
    public static List<LatLng> getAfterPartCyclePosition(LatLng startPositoin, LatLng endPostion){
        List<LatLng> afterCycle=new ArrayList<>();
        double y1=startPositoin.latitude;
        double y2=endPostion.latitude;
        double x1=startPositoin.longitude;
        double x2=endPostion.longitude;
        double middlePointX=(x1+x2)/2;
        double middlePointY=(y1+y2)/2;
        double heightDifference=(y2-y1)/6/share;
        double widthDifference=(x2-x1)/2/share;
        for(int i=0;i<share;i++){
            afterCycle.add(new LatLng(middlePointY+i*heightDifference,middlePointX+i*widthDifference));
        }
        return afterCycle;
    }
}
