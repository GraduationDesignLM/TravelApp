package com.mao.travelapp;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.j256.ormlite.dao.Dao;
import com.mao.imageloader.ImageLoader;
import com.mao.imageloader.core.ImageLoaderOptions;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.sdk.UploadFileCallback;
import com.mao.travelapp.sdk.FileHelper;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);

        //文件测试
        String path = "/sdcard/abc.jpg";
        FileHelper.uploadOne(path, new UploadFileCallback() {

            @Override
            public void onSuccess(String url) {

                System.out.println("url:" + url);
                ImageLoader.getInstance().displayImage(MainActivity.this, url, iv);
            }

            @Override
            public void onFail(String error) {
                System.out.println("error:" + error);
            }
        });


    }

}
