package com.mao.travelapp.bean;

import com.j256.ormlite.field.DatabaseField;
import com.mao.travelapp.sdk.BaseObject;

/**
 * Created by mao on 2016/11/9.
 */
public class User extends BaseObject {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String username;

    @DatabaseField
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
