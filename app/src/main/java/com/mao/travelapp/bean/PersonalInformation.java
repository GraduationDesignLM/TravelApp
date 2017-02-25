package com.mao.travelapp.bean;

import com.mao.travelapp.sdk.BaseObject;

/**
 * Created by lyw on 2017/2/20.
 */

public class PersonalInformation extends BaseObject{
    private int id;
    private String introduction;
    private String gender;
    private String name;
    private String headUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
