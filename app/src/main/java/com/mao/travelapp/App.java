package com.mao.travelapp;

import android.app.Application;

import com.mao.travelapp.manager.AppAccessToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.v3.Bmob;

/**
 * Created by mao on 2016/11/29.
 */
public class App extends Application {

    /** 测试模式 */
    public static final boolean DEBUG = true;
    /** 是否进行自动登录 */
    public static final boolean AUTO_LOGIN = true;

    public static final String Baidu_AK = "wghVwKkcuFXcmbIxOkOPzBvZoMwljPlE";
    public static final String SHA1_PACKAGE = "F6:D2:5E:73:A0:2E:66:EA:C5:7D:82:F8:99:BF:69:6C:91:7D:C1:55;com.mao.travelapp";

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(getApplicationContext(), AppAccessToken.APPLICATION_ID);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

    }
}
