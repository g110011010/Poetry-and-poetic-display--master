package com.example.lbstest.Entity;

/**
 * Created by 天道 北云 on 2017/9/3.
 */

public class Poet {
    private String name;
    private String nickname;
    private int imageId;
    public Poet(String name,String nickname,int imageId){
        this.name=name;
        this.nickname=nickname;
        this.imageId=imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
