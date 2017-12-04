package com.example.lbstest.Controller;

import android.util.Log;

import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.example.lbstest.DateBase.LI_BAI;
import com.example.lbstest.Entity.Poet;
import com.example.lbstest.MapActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理按诗人地址展示
 * Created by 天道 北云 on 2017/9/17.
 * @author BeiYum
 */

public class VenueDisplayController {
    /**
     * 用于返回数据库对应诗人所有的相关年份。
     * @param li_bais 诗人的详细信息列表。
     * @return 对应诗人所有的相关年份。
     */
    static public List<Integer> getAllYear(List<LI_BAI> li_bais){
        int lastYear=-1;
        int currentYear;
        List<Integer> allYear=new ArrayList<>();
        for(LI_BAI temp:li_bais){
            currentYear=temp.getYear();
            if(temp.getWorksType().equals("")){
                continue;
            }else {
                if (currentYear == lastYear) {
                    continue;
                } else {
                    lastYear = currentYear;
                    allYear.add(new Integer(currentYear));
                }
            }
        }
        return allYear;
    }

    /**
     * 用于在得到地图依时间地图展示所需年份后对地图修改以及展示。
     * @param baiduMap 需要进行操作的BaiduMap对象。
     * @param mClusterManager 需要重新进行点聚合运算的点聚合管理器。
     * @param year 指定年份。
     */
    static public void VenueDisplay(BaiduMap baiduMap, ClusterManager mClusterManager, Integer year){
        ForMapSpanManagerController mForMapSpanManager = new ForMapSpanManagerController(baiduMap);
        List<LatLng> mapzoom=new ArrayList<>();
        List<MapActivity.poetry> poems=new ArrayList<>();
        int needHandleYear=year.intValue();
        List<LI_BAI> records=DataSupport.where("year = ?",""+needHandleYear).find(LI_BAI.class);
        for(LI_BAI record:records){
            LatLng latLng=new LatLng(record.getLatitude(),record.getLongitude());
            if(record.getWorksType().equals("")){
                continue;
            }else if(!record.getPoetryTitle().equals("")){
                poems.add(new MapActivity.poetry(latLng ,record.getPoetryTitle(), "对应内容尚未收录"));
                mapzoom.add(latLng);
            }
        }
        mForMapSpanManager.setScreenList(mapzoom);
        mForMapSpanManager.addToMap();
        mForMapSpanManager.zoomToSpan();
        baiduMap.clear();
        mClusterManager.clearItems();
        mClusterManager.cluster();
        mClusterManager.addItems(poems);
    }
}
