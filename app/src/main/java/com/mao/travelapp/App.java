package com.mao.travelapp;

import android.app.Application;

import com.mao.imageloader.ImageLoader;
import com.mao.imageloader.core.ImageLoaderConfiguration;

/**
 * Created by mao on 2016/11/29.
 */
public class App extends Application {

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
