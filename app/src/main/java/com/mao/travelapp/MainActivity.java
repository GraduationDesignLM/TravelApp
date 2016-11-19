package com.mao.travelapp;

import android.content.SyncStatusObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mao.travelapp.bean.User;
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


        User user = new User();
        Map<String, String> where = new HashMap<String, String>();
        where.put("id", "3");
        user.query(where, User.class, new QueryCallback<User>() {

            @Override
            public void onSuccess(List<User> list) {
                System.out.println(list.size());
                System.out.println(list.get(0).getClass());
                for(User u : list) {
                    System.out.println("id:" + u.getId() + ",username:" + u.getUsername() + ",password:" + u.getPassword());
                }
            }

            @Override
            public void onFail(String error) {
                System.out.println(error);
            }
        });



    }

}
