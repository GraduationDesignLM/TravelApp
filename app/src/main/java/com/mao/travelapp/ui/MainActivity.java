package com.mao.travelapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.mao.imageloader.ImageLoader;
import com.mao.travelapp.R;
import com.mao.travelapp.sdk.FileHelper;
import com.mao.travelapp.sdk.UploadFileCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LocationBaseActivity{

    private ImageView iv;
    Button btn_publish;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initEvent();

//        //文件测试
//        List<String> paths = new ArrayList<String>();
//        paths.add("/sdcard/abc.jpg");
//        paths.add("/sdcard/123.jpg");
//        FileHelper.upload(paths, new UploadFileCallback() {
//
//            @Override
//            public void onSuccess(List<String> urls) {
//
//                for (String s : urls) {
//                    System.out.println("url:" + s);
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                System.out.println("error:" + error);
//            }
//        });


    }

    @Override
    void receiveLocation(BDLocation location) {
        Log.v(TAG,location.getAddrStr());
    }

    private void initEvent() {
        btn_publish = (Button) findViewById(R.id.btn_publish);
        iv = (ImageView) findViewById(R.id.iv);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PublishMainActivity.class);
                startActivity(intent);
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> paths = new ArrayList<String>();
//                paths.add("/sdcard/123.jpg");
//                paths.add("/sdcard/abc.jpg");
                paths.add("/storage/sdcard/Download/images-6.jpg");
                paths.add("/storage/sdcard/Download/images-7.jpg");
                FileHelper.upload(paths, new UploadFileCallback() {

                    @Override
                    public void onSuccess(List<String> urls) {

                        for (String s : urls) {
                            System.out.println("url:" + s);
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        System.out.println("error:" + error);
                    }
                });
            }
        });
    }
}