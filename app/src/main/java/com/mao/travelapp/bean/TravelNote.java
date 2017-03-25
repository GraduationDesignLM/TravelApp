package com.mao.travelapp.bean;

import com.j256.ormlite.field.DatabaseField;
import com.mao.travelapp.sdk.BaseObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyw on 2017/2/20.
 */

public class TravelNote extends BaseObject implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String text;
    @DatabaseField
    private String pictureUrls;
    @DatabaseField
    private String location;
    @DatabaseField
    private int userId;
    @DatabaseField
    private String publish_time;
    @DatabaseField
    private double latitude;
    @DatabaseField
    private double longitude;

    public TravelNote(){}

    public TravelNote(String text, String pictureUrls,
                      String location, int userId,
                      String publish_time, double latitude,
                      double longitude) {
        this.text = text;
        this.pictureUrls = pictureUrls;
        this.location = location;
        this.userId = userId;
        this.publish_time = publish_time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(String pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
