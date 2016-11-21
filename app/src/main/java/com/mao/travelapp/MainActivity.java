package com.mao.travelapp;

import android.content.SyncStatusObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mao.travelapp.bean.User;
import com.mao.travelapp.sdk.BaseObject;
import com.mao.travelapp.sdk.CommonCallback;
import com.mao.travelapp.sdk.QueryCallback;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加测试
//        User user = new User();
//        user.setUsername("gou");
//        user.setPassword("123456");
//        user.save(new CommonCallback() {
//            @Override
//            public void onSuccess(int affectedRowCount) {
//                System.out.println("affectedRowCount:" + affectedRowCount);
//            }
//
//            @Override
//            public void onFail(String error) {
//                System.out.println("error:" + error);
//            }
//        });

        //更新测试

        //删除测试
        Map<String, String> where = new HashMap<String, String>();
        //where.put("id", "1");
        BaseObject.delete(where, User.class, new CommonCallback() {
            @Override
            public void onSuccess(int affectedRowCount) {
                System.out.println("affectedRowCount:" + affectedRowCount);
            }

            @Override
            public void onFail(String error) {
                System.out.println("error:" + error);
            }
        });

        //查询测试
        /*Map<String, String> where = new HashMap<String, String>();
        BaseObject.query(where, User.class, new QueryCallback<User>(){

            @Override
            public void onSuccess(List<User> list) {
                for(User u : list) {
                    System.out.println(u.getUsername() + " " + u.getPassword());
                }
            }

            @Override
            public void onFail(String error) {
                System.out.println(error);
            }
        });*/



    }

}
