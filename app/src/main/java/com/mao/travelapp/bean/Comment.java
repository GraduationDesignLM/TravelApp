package com.mao.travelapp.bean;

import com.mao.travelapp.sdk.BaseObject;

/**
 * 评论
 *
 * Created by mao on 2017/3/27.
 */
public class Comment extends BaseObject{

    private int id;

    private String username;

    private String date;

    private String text;

    private int pubId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPubId() {
        return pubId;
    }

    public void setPubId(int pubId) {
        this.pubId = pubId;
    }
}
