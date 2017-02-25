package com.mao.travelapp.manager;

import com.mao.travelapp.bean.User;

/**
 * Created by mao on 2016/12/1.
 */
public class UserManager {

    private static User sInstance;

    public synchronized  static User getInstance() {
        return sInstance;
    }

    public synchronized static void setInstance(User user) {
        sInstance = user;
    }

    public synchronized static boolean isLogin() {
        return sInstance != null;
    }
}
