package com.mao.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mao.travelapp.bean.User;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        User user = new User();
        user.setUsername("mao");
        user.setPassword("123456");
        user.save();



    }

}
