package com.mao.travelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mao.imageloader.ImageLoader;
import com.mao.travelapp.R;
import com.mao.travelapp.sdk.FileHelper;
import com.mao.travelapp.sdk.UploadFileCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ImageView iv;
    Button btn_publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
        iv = (ImageView) findViewById(R.id.iv);

        //文件测试
        List<String> paths = new ArrayList<String>();
        paths.add("/sdcard/abc.jpg");
        paths.add("/sdcard/123.jpg");
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

    private void initEvent(){
        btn_publish = (Button) findViewById(R.id.btn_publish);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,PublishMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setToolbaroOpposition() {

    }

    @Override
    protected int getToolbarMenuLayout() {
        return 0;
    }

    @Override
    protected void setBaseToolbarMenuItemClickListener() {

    }

}
