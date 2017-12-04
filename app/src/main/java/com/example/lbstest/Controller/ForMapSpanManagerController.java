package com.example.lbstest.Controller;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.example.lbstest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 此函数用于实现将指定的点以合适的比例缩放到合适的视野内（经过了缩放和移动）
 * Created by BeiYun on 2017/8/27.
 * @author BeiYun
 */

public class ForMapSpanManagerController extends OverlayManager{
    List<OverlayOptions> mOverlayOptions= new ArrayList<OverlayOptions>();
    public ForMapSpanManagerController(BaiduMap baiduMap) {
        super(baiduMap);
    }
    public void setScreenList(List<LatLng> screenList){
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        for(LatLng temp : screenList){
            mOverlayOptions.add(new MarkerOptions().position(temp).icon(bitmapDescriptor));
        }
    }
    @Override
    public List<OverlayOptions> getOverlayOptions() {
        return mOverlayOptions;
    }

    @Override
    public void zoomToSpan() {
        super.zoomToSpan();
    }



    @Override
    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
