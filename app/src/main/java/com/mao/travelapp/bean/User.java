package com.mao.travelapp.bean;

import com.mao.travelapp.sdk.BaseObject;

/**
 * Created by mao on 2016/11/9.
 */
public class User extends BaseObject {

    private String username;
    private String password;

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
