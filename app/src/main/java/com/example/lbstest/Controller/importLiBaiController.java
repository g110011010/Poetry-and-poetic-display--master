package com.example.lbstest.Controller;

import android.content.Context;
import android.util.Log;

import com.example.lbstest.DateBase.LI_BAI;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.List;

/**
 * 此类用于将LIBAI对应的Excel信息导入到LitePal架构的数据库中所需的控制类。
 * Created by BeiYun on 2017/9/5.
 * @author BeiYun
 */

public class importLiBaiController {
    private static final String TAG = "importLiBaiController";
     static public void importInto(Context context){
        try {

            InputStream is = context.getResources().getAssets().open("li_bai.xls");
            AnalyExcelAndInsertIntoDataBaseForLiBai.AnalyAndInsert(is);
        }catch(Exception e){
            Log.d(TAG,e.toString());
        }
        List<LI_BAI> records= DataSupport.findAll(LI_BAI.class);
        for(LI_BAI record:records){
            Log.d(TAG,"题目"+record.getPoetryTitle());
            Log.d(TAG,"年份"+record.getYear());
            Log.d(TAG,"纬度"+record.getLatitude());
            Log.d(TAG,"经度"+record.getLongitude());
        }
    }
}
