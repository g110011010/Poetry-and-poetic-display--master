package com.example.lbstest.DateBase;

import org.litepal.crud.DataSupport;

/**
 * 此类为LI_BAI的实体类，也作为LitePal映射的实体类。
 * Created by BeiYun on 2017/9/1.
 * @author BeiYun
 */

public class LI_BAI extends DataSupport{
    private int year;
    private int age;
    private String month;
    private String name;
    private String Dynasty;
    private String gender;
    private String particularYear;
    private String archaicAvenue;
    private String archaicState;
    private String archaicCounty;
    private String scenic;
    private String province;
    private String county;
    private String position;
    private String activityContentOrCreationOrigins;
    private String namesOffriends;
    private String works;
    private String worksType;
    private String accordingToTheSource;
    private String pageNumber;
    private String specialRemarks;
    private String poetryTitle;
    private String poetryContent;
    private double longitude;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDependence() {
        return dependence;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    private String dependence;


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getActivityContentOrCreationOrigins() {
        return activityContentOrCreationOrigins;
    }

    public void setActivityContentOrCreationOrigins(String activityContentOrCreationOrigins) {
        this.activityContentOrCreationOrigins = activityContentOrCreationOrigins;
    }

   public String getPoetryContent() {
        return poetryContent;
    }

    public void setPoetryContent(String poetryContent) {
        this.poetryContent = poetryContent;
    }

    public String getPoetryTitle() {
        return poetryTitle;
    }

    public void setPoetryTitle(String poetryTitle) {
        this.poetryTitle = poetryTitle;
    }

    public String getAccordingToTheSource() {
        return accordingToTheSource;
    }

    public void setAccordingToTheSource(String accordingToTheSource) {
        this.accordingToTheSource = accordingToTheSource;
    }

    public String getActivityContentRrCreationOrigin() {
        return activityContentOrCreationOrigins;
    }

    public void setActivityContentRrCreationOrigin(String activityContentRrCreationOrigin) {
        this.activityContentOrCreationOrigins = activityContentRrCreationOrigin;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getArchaicAvenue() {
        return archaicAvenue;
    }

    public void setArchaicAvenue(String archaicAvenue) {
        this.archaicAvenue = archaicAvenue;
    }

    public String getArchaicCounty() {
        return archaicCounty;
    }

    public void setArchaicCounty(String archaicCounty) {
        this.archaicCounty = archaicCounty;
    }

    public String getArchaicState() {
        return archaicState;
    }

    public void setArchaicState(String archaicState) {
        this.archaicState = archaicState;
    }

    public String getDynasty() {
        return Dynasty;
    }

    public void setDynasty(String dynasty) {
        Dynasty = dynasty;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamesOffriends() {
        return namesOffriends;
    }

    public void setNamesOffriends(String namesOffriends) {
        this.namesOffriends = namesOffriends;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getParticularYear() {
        return particularYear;
    }

    public void setParticularYear(String particularYear) {
        this.particularYear = particularYear;
    }


    public String getScenic() {
        return scenic;
    }

    public void setScenic(String scenic) {
        this.scenic = scenic;
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public String getWorksType() {
        return worksType;
    }

    public void setWorksType(String worksType) {
        this.worksType = worksType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
