package com.mao.travelapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mao.travelapp.bean.User;

/**
 *
 * SharePreferences管理类
 *
 * Created by mao on 2017/2/25.
 */
public class SpManager {

    /* 用户信息SharePreferences文件名 **/
    public static final String USER_SP_NAME = "user";

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String PHONE_KEY = "phone";

    public static void saveUser(Context context, User user) {
        if(context == null || user == null) {
            return;
        }
        String username = user.getUsername();
        String password = user.getPassword();
        String phone = user.getPhone();

        SharedPreferences sp = context.getSharedPreferences(SpManager.USER_SP_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SpManager.USERNAME_KEY, username);
        editor.putString(SpManager.PASSWORD_KEY, password);
        editor.putString(SpManager.PHONE_KEY, phone);
        editor.commit();
    }

    public static User getUser(Context context) {
        if(context == null) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(SpManager.USER_SP_NAME, Context.MODE_PRIVATE);
        String username = sp.getString(SpManager.USERNAME_KEY, null);
        String password = sp.getString(SpManager.PASSWORD_KEY, null);
        String phone = sp.getString(SpManager.PHONE_KEY, null);

        if((!TextUtils.isEmpty(username) || !TextUtils.isEmpty(phone))
                && !TextUtils.isEmpty(password)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            return user;
        } else {
            return null;
        }
    }
}

