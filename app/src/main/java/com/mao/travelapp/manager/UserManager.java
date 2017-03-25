package com.mao.travelapp.manager;

import android.content.Context;
import android.text.TextUtils;

import com.mao.travelapp.bean.User;
import com.mao.travelapp.sdk.BaseObject;
import com.mao.travelapp.sdk.CommonDBCallback;
import com.mao.travelapp.sdk.QueryCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    public static void login(User user, final OnLoginListener listener) {
        if(user == null) {
            if(listener != null) {
                listener.onFail("参数错误");
            }
            return;
        }
        Map<String, String> where = new HashMap<String, String>();
        String username = user.getUsername();
        String phone = user.getPhone();
        String password = user.getPassword();
        if(!TextUtils.isEmpty(username)) {
            where.put("username", username);
        }
        if(!TextUtils.isEmpty(phone)) {
            where.put("phone", phone);
        }
        if(!TextUtils.isEmpty(password)) {
            where.put("password", password);
        }
        BaseObject.query(where, User.class, new QueryCallback<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list != null && list.size() > 0) {
                    //登录成功
                    if(listener != null) {
                        listener.onSuccess(list.get(0));
                    }
                } else {
                    if(listener != null) {
                        listener.onFail("账号或者密码不正确，请重新输入");
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(listener != null) {
                    listener.onFail("账号或者密码不正确，请重新输入");
                }
            }
        });
    }

    /**
     * 退出登录
     *
     * @return 退出登录成功返回true，失败返回false
     */
    public static boolean unLogin(Context context) {
        if(context == null) {
            return false;
        }
        try {
            UserManager.setInstance(null);
            SpManager.saveUser(context, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void register(final User user, final OnRegisterListener listener) {
        if(user == null) {
            if(listener != null) {
                listener.onFail("参数错误");
            }
            return;
        }
        //简单起见，先不管
//        Map<String, String> where = new HashMap<String, String>();
//        String username = user.getUsername();
//        String phone = user.getPhone();
//        String password = user.getPassword();
//        if(!TextUtils.isEmpty(username)) {
//            where.put("username", username);
//        }
//        if(!TextUtils.isEmpty(phone)) {
//            where.put("phone", phone);
//        }
//        if(!TextUtils.isEmpty(password)) {
//            where.put("password", password);
//        }
//        BaseObject.query(where, User.class, new QueryCallback<User>() {
//            @Override
//            public void onSuccess(List<User> list) {
//
//            }
//
//            @Override
//            public void onFail(String error) {
//
//            }
//        }
        user.save(new CommonDBCallback() {
            @Override
            public void onSuccess(int affectedRowCount) {
                if(affectedRowCount == 1 && listener != null) {
                    listener.onSuccess(user);
                }
            }

            @Override
            public void onFail(String error) {
                if(listener != null) {
                    listener.onFail(error);
                }
            }
        });

    }

    public interface OnRegisterListener {

        void onSuccess(User user);

        void onFail(String error);
    }

    public interface OnLoginListener {

        void onSuccess(User user);

        void onFail(String error);
    }
}
