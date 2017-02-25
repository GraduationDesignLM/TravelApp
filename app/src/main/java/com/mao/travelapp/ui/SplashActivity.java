package com.mao.travelapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.mao.travelapp.R;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.SpManager;
import com.mao.travelapp.manager.UserManager;
import com.mao.travelapp.sdk.BaseObject;
import com.mao.travelapp.sdk.QueryCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动页
 *
 * Created by mao on 2017/2/25.
 */
public class SplashActivity extends BaseActivity {

    private Button mLoginBtn;
    private Button mRegBtn;
    private View mOperationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        boolean isLogin = false;
        //检查有没有登录
        if (UserManager.isLogin()) {
            //已登录则开启首页
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        //是否可以进行自动登录
        SharedPreferences sp = getSharedPreferences(SpManager.USER_SP_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SpManager.USERNAME_KEY, "安琪拉");
        editor.putString(SpManager.PASSWORD_KEY, "123456");
        editor.commit();

        final User rawUser = new User();
        rawUser.setUsername(sp.getString(SpManager.USERNAME_KEY, null));
        rawUser.setPassword(sp.getString(SpManager.PASSWORD_KEY, null));
        Map<String, String> where = new HashMap<String, String>();
        where.put("username", rawUser.getUsername());
        where.put("password", rawUser.getPassword());
        BaseObject.query(where, User.class, new QueryCallback<User>() {

            @Override
            public void onSuccess(List<User> list) {
                System.out.println(111);
                if (list != null && list.size() > 0) {
                    User user = list.get(0);
                    //再次检验
                    if (user != null
                            && !TextUtils.isEmpty(user.getUsername())
                            && !TextUtils.isEmpty(user.getPassword())) {
                        if (user.getUsername().equals(rawUser.getUsername())
                                && user.getPassword().equals(rawUser.getPassword())) {
                            //登录成功
                            UserManager.setInstance(user);
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
                handleForUnLoagin();
            }

            @Override
            public void onFail(String error) {
                System.out.println(error);
                handleForUnLoagin();
            }
        });
    }

    private void initView() {
        mOperationView = findViewById(R.id.operationView);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mRegBtn = (Button) findViewById(R.id.regBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录页面
            }
        });

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
            }
        });

    }

    private void handleForUnLoagin() {
        mOperationView.setVisibility(View.VISIBLE);
    }
}
