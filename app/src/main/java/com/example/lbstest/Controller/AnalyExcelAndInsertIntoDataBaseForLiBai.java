package com.example.lbstest.Controller;

import android.util.Log;

import com.example.lbstest.DateBase.LI_BAI;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;


/**
 * Created by BeiYun on 2017/9/3.
 */

public class AnalyExcelAndInsertIntoDataBaseForLiBai {
    private static final String TAG = "AnalyExcelAndInsertInto";
    public static void  AnalyAndInsert(InputStream stream){
        try {
            Workbook book = Workbook.getWorkbook(stream);
            book.getNumberOfSheets();
            Sheet sheet=book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();

            Log.d(TAG,"表名称"+sheet.getName());
            Log.d(TAG,"表行数"+Rows);
            Log.d(TAG,"表名称"+Cols);
            String tempAcceptString;
            double tempAcceptDouble;
            for(int i=0;i<Rows;++i){
                Log.d(TAG,"第    "+i+"条"+"  数据");
                LI_BAI liBai=new LI_BAI();
                tempAcceptString=sheet.getCell(0,i).getContents();
                Log.d(TAG,"0"+tempAcceptString);
                if(tempAcceptString==null){
                    liBai.setToDefault("Dynasty");
                }else {
                    liBai.setDynasty(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(1,i).getContents();
                Log.d(TAG,"1"+tempAcceptString);
                if(tempAcceptString==null){
                    liBai.setToDefault("ParticularYear");
                }else {
                    liBai.setParticularYear(tempAcceptString);
                }
                liBai.setYear(Integer.valueOf(sheet.getCell(2,i).getContents()));
                Log.d(TAG,"2"+sheet.getCell(2,i).getContents());
                tempAcceptString=sheet.getCell(3,i).getContents();
                Log.d(TAG,"3"+tempAcceptString);
                if(tempAcceptString==null){
                    liBai.setToDefault("Month");
                }else{
                    liBai.setMonth(tempAcceptString);
                }
                liBai.setAge(Integer.valueOf(sheet.getCell(5,i).getContents()));
                Log.d(TAG,"5"+sheet.getCell(5,i).getContents());
                tempAcceptString=sheet.getCell(7,i).getContents();
                Log.d(TAG,"7"+tempAcceptString);
                if(tempAcceptString==null){
                    liBai.setToDefault("ArchaicAvenue");
                }else {
                    liBai.setArchaicAvenue(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(8, i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("ArchaicState");
                }else {
                    liBai.setArchaicState(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(9,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("ArchaicCounty");
                }else {
                    liBai.setArchaicCounty(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(10,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("Scenic");
                }else {
                    liBai.setScenic(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(11,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("Province");
                }else {
                    liBai.setProvince(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(12,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("County");
                }else {
                    liBai.setCounty(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(13,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("Position");
                }else {
                    liBai.setPosition(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(14,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("ActivityContentOrCreationOrigins");
                }else{
                    liBai.setActivityContentOrCreationOrigins(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(15,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("NamesOffriends");
                }else {
                    liBai.setNamesOffriends(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(16, i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("poetryTitle");
                }else {
                    liBai.setPoetryTitle(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(17,i).getContents();
                if(tempAcceptString==null) {
                    liBai.setToDefault("WorksType");
                }else{
                    liBai.setWorksType(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(18,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("Dependence");
                }else {
                    liBai.setDependence(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(19,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("PageNumber");
                }else {
                    liBai.setPageNumber(tempAcceptString);
                }
                tempAcceptString=sheet.getCell(20,i).getContents();
                if(tempAcceptString==null){
                    liBai.setToDefault("SpecialRemarks");
                }else {
                    liBai.setSpecialRemarks(tempAcceptString);
                }
                tempAcceptDouble=Double.valueOf(sheet.getCell(23,i).getContents());
                if(tempAcceptDouble==0.0){
                    liBai.setToDefault("Longitude");
                }else{
                    liBai.setLongitude(tempAcceptDouble);
                }
                tempAcceptDouble=Double.valueOf(sheet.getCell(24,i).getContents());
                if(tempAcceptDouble==0.0){
                    liBai.setToDefault("Latitude");
                }else{
                    liBai.setLatitude(tempAcceptDouble);
                }
                liBai.save();
            }
            book.close();
        }catch(Exception e){
            Log.d(TAG,e.toString());
        }
    }

}
