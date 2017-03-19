package com.mao.travelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mao.travelapp.App;
import com.mao.travelapp.R;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.SpManager;
import com.mao.travelapp.manager.UserManager;

/**
 * 启动页
 *
 * Created by mao on 2017/2/25.
 */
public class SplashActivity extends BaseActivity {

    private static final int REGISTER_REQUEST_CODE = 1;
    private static final int LOGIN_REQUEST_CODE = 2;

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

        //不进行自动登录，开发调试使用，发布时必须把AUTO_LOGIN设置为true
        if(!App.AUTO_LOGIN) {
            handleForUnLoagin();
            return;
        }

        final User rawUser = SpManager.getUser(this);
        UserManager.login(rawUser, new UserManager.OnLoginListener() {
            @Override
            public void onSuccess(User user) {
                UserManager.setInstance(user);
                SpManager.saveUser(SplashActivity.this, user);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {
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
                startActivityForResult(new Intent(SplashActivity.this, LoginActivity.class), 1);
            }
        });

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                startActivityForResult(new Intent(SplashActivity.this, RegisterActivity.class), 1);
            }
        });

    }

    private void handleForUnLoagin() {
        mOperationView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //检查有没有登录，有的话finish调当前Activity，然后跳转到MainActivity
        if(UserManager.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
