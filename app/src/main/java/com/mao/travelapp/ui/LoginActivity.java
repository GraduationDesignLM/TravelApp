package com.mao.travelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mao.imageloader.utils.EncryptHelper;
import com.mao.travelapp.R;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.UserManager;

/**
 * Created by mao on 2017/3/5.
 *
 * 登录页面
 *
 */
public class LoginActivity extends BaseActivity {

    private EditText app_login_username_phone_et;
    private EditText app_login_password_et;
    private Button app_login_btn;
    private TextView app_login_login_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initActionBar();

        initView();

        setViewEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //判断是否已登录，如果已登录则finish掉当前Activity
        if(UserManager.isLogin()) {
            finish();
        }
    }

    private void initActionBar() {
        setActionBarCenterText(R.string.login);
        TextView registerTv = setActionBarRightText(R.string.register);
        if(registerTv != null) {
            registerTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //跳转到注册页面
                    startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);
                }
            });
        }
    }

    private void initView() {
        app_login_username_phone_et = (EditText) findViewById(R.id.app_login_username_phone_et);
        app_login_password_et = (EditText) findViewById(R.id.app_login_password_et);
        app_login_btn = (Button) findViewById(R.id.app_login_btn);
        app_login_login_tip = (TextView) findViewById(R.id.app_login_login_tip);
    }

    private void setViewEvent() {
        app_login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String usernamePhone = app_login_username_phone_et.getText().toString().trim();
                String password = app_login_password_et.getText().toString().trim();
                if(!TextUtils.isEmpty(usernamePhone) && !TextUtils.isEmpty(password)) {
                    password = EncryptHelper.md5(password);
                    if(!TextUtils.isEmpty(password)) {
                        User user = new User();
                        //先判断是用户名还是手机号
                        if(usernamePhone.length() > 10) {
                            user.setPhone(usernamePhone);
                        } else {
                            user.setUsername(usernamePhone);
                        }
                        login(user);
                    }
                } else {
                    if(TextUtils.isEmpty(usernamePhone)) {
                        app_login_login_tip.setText("请输入手机号码或者用户名");
                    } else if(TextUtils.isEmpty(password)) {
                        app_login_login_tip.setText("请输入密码");
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //已登录
        if (UserManager.isLogin()) {
            //启动MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(User user) {
        //处理登录逻辑
    }
}
