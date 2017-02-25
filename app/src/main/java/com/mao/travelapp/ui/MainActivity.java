package com.mao.travelapp.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.mao.imageloader.ImageLoader;
import com.mao.travelapp.R;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.sdk.CommonDBCallback;
import com.mao.travelapp.sdk.FileHelper;
import com.mao.travelapp.sdk.UploadFileCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        iv = (ImageView) findViewById(R.id.iv);

        //文件测试
        List<String> paths = new ArrayList<String>();
        paths.add("/sdcard/abc.jpg");
        paths.add("/sdcard/123.jpg");
        FileHelper.upload(paths, new UploadFileCallback() {

            @Override
            public void onSuccess(List<String> urls) {

                for(String s : urls) {
                    System.out.println("url:" + s);
                }
            }

            @Override
            public void onFail(String error) {
                System.out.println("error:" + error);
            }
        });


    }

}
