package com.mao.travelapp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.travelapp.App;
import com.mao.travelapp.R;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.SpManager;
import com.mao.travelapp.manager.UserManager;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

/**
 * Created by mao on 2017/3/5.
 *
 * 注册页面
 *
 */
public class RegisterActivity extends BaseActivity {

    private EditText app_register_username_et;
    private EditText app_register_phone_et;
    private EditText app_register_password_et;
    private EditText app_register_validatecode_et;
    private TextView app_register_validatecode_tv;
    private Button app_finish_btn;
    private TextView app_register_tip;

    //验证码
    private Integer smsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initActionBar();

        initView();

        setEvent();
    }

    private void initActionBar() {
        setActionBarCenterText(R.string.register);
    }

    private void initView() {
        app_register_username_et = (EditText) findViewById(R.id.app_register_username_et);
        app_register_phone_et = (EditText) findViewById(R.id.app_register_phone_et);
        app_register_password_et = (EditText) findViewById(R.id.app_register_password_et);
        app_register_validatecode_et = (EditText) findViewById(R.id.app_register_validatecode_et);
        app_register_validatecode_tv = (TextView) findViewById(R.id.app_register_validatecode_tv);
        app_finish_btn = (Button) findViewById(R.id.app_finish_btn);
        app_register_tip = (TextView) findViewById(R.id.app_register_tip);

        enableValidateCodeTv(true);
    }

    private void setEvent() {
        //发送验证码
        app_register_validatecode_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phone = app_register_phone_et.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    setRegisterError("请输入手机号码");
                } else {
                    sendValidateCode(phone);
                }
            }
        });
        //尝试注册
        app_finish_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //注册
                register();
            }
        });
    }

    private void register() {
        String username = app_register_username_et.getText().toString().trim();
        String phone = app_register_phone_et.getText().toString().trim();
        String password = app_register_password_et.getText().toString().trim();
        String validateCode = app_register_validatecode_et.getText().toString().trim();

        if(TextUtils.isEmpty(username)) {
            setRegisterError("请输入用户名");
        } else if(TextUtils.isEmpty(phone)) {
            setRegisterError("请输入手机号码");
        } else if(TextUtils.isEmpty(password)) {
            setRegisterError("请输入密码");
        } else if(!App.DEBUG && TextUtils.isEmpty(validateCode)) {
            setRegisterError("请输入验证码");
        } else {
            startRegister(username, phone, password, validateCode);
        }
    }

    private void sendValidateCode(String phone) {
        BmobSMS.requestSMSCode(getApplicationContext(), phone, "注册验证码", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException e) {
                //成功
                if(e == null) {
                    handleForValidateCodeSendSuccessFully(smsId);
                }
            }
        });
    }

    private void setRegisterError(String text) {
        app_register_tip.setText(text);
    }

    private void handleForValidateCodeSendSuccessFully(Integer smsId) {
        this.smsId = smsId;
        Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
        enableValidateCodeTv(false);
        new ValidateCodeUpdateThread(60).start();
    }

    //设置获取验证码按钮是否可用
    private void enableValidateCodeTv(boolean enable) {
        Resources res = getResources();
        if(enable) {
            app_register_validatecode_tv.setClickable(true);
            app_register_validatecode_tv.setBackgroundColor(res.getColor(R.color.app_main_color));
        } else {
            app_register_validatecode_tv.setClickable(false);
            app_register_validatecode_tv.setBackgroundColor(res.getColor(R.color.app_common_button_selBackgroundColor));
        }
    }

    private void updateValidateCodeTv(int remainTime) {
        String reGet = "重新发送";
        StringBuilder sb = new StringBuilder(reGet);
        if(remainTime <= 0) {
            enableValidateCodeTv(true);
        } else {
            sb.append("(");
            sb.append(remainTime);
            sb.append("s)");
        }
        app_register_validatecode_tv.setText(sb.toString());
    }

    //开始注册
    private void startRegister(final String username, final String phone, final String password, String validateCode) {

        //测试阶段不用验证码
        if(App.DEBUG) {
            registerNewUser(username, phone, password);
            return;
        }

        //先验证验证码是否正确
        BmobSMS.verifySmsCode(getApplicationContext(), phone, validateCode, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException e) {
                if(e == null) {
                    registerNewUser(username, phone, password);
                } else {
                    setRegisterError("验证码错误");
                }
            }
        });
    }

    //注册新用户
    private void registerNewUser(final String username, final String phone, final String password) {
        //处理注册逻辑
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        UserManager.register(user, new UserManager.OnRegisterListener() {
            @Override
            public void onSuccess(User user) {
                UserManager.setInstance(user);
                SpManager.saveUser(RegisterActivity.this, user);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //验证码TextView更新线程
    private class ValidateCodeUpdateThread extends Thread {

        private int mTotalSecond;
        private int mPastSecond;

        public ValidateCodeUpdateThread(int total) {
            mTotalSecond = total;
            mPastSecond = 0;
        }

        @Override
        public void run() {
            while(mPastSecond <= mTotalSecond) {
                app_register_validatecode_tv.post(new Runnable() {

                    @Override
                    public void run() {
                        updateValidateCodeTv(mTotalSecond - mPastSecond);
                    }
                });
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPastSecond++;
            }
        }
    }
}
