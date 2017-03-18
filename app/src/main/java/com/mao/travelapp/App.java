package com.mao.travelapp;

import android.app.Application;

import com.mao.imageloader.ImageLoader;
import com.mao.imageloader.core.ImageLoaderConfiguration;

/**
 * Created by mao on 2016/11/29.
 */
public class App extends Application {

    public static final String Baidu_AK = "wghVwKkcuFXcmbIxOkOPzBvZoMwljPlE";
    public static final String SHA1_PACKAGE = "F6:D2:5E:73:A0:2E:66:EA:C5:7D:82:F8:99:BF:69:6C:91:7D:C1:55;com.mao.travelapp";

    @Override
    public void onCreate() {
        super.onCreate();


        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder()
                .setDiskCacheMaxSize(2 * 1024 * 1024 * 1024L)
                .setDiskCachePath("/sdcard/TravelApp/pic/")
                .isAutoCreateCacheDir(true)
                .build();
        imageLoader.setImageLoaderConfiguration(config);
    }
}
