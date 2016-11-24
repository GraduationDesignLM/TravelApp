package com.mao.travelapp;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.j256.ormlite.dao.Dao;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.sdk.UploadFileCallback;
import com.mao.travelapp.sdk.FileHelper;

import db.OrmDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //添加测试
////        User user = new User();
////        user.setUsername("gou");
////        user.setPassword("123456");
////        user.save(new CommonDBCallback() {
////            @Override
////            public void onSuccess(int affectedRowCount) {
////                System.out.println("affectedRowCount:" + affectedRowCount);
////            }
////
////            @Override
////            public void onFail(String error) {
////                System.out.println("error:" + error);
////            }
////        });
//
//        //更新测试
//
//        //删除测试
//        Map<String, String> where = new HashMap<String, String>();
//        //where.put("id", "1");
//        BaseObject.delete(where, User.class, new CommonDBCallback() {
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
//
//        //查询测试
//        /*Map<String, String> where = new HashMap<String, String>();
//        BaseObject.query(where, User.class, new QueryCallback<User>(){
//
//            @Override
//            public void onSuccess(List<User> list) {
//                for(User u : list) {
//                    System.out.println(u.getUsername() + " " + u.getPassword());
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                System.out.println(error);
//            }
//        });*/

        iv = (ImageView) findViewById(R.id.iv);


//        //文件测试
//        String path = "/sdcard/123.jpg";
//        FileHelper.uploadOne(path, new UploadFileCallback() {
//
//            @Override
//            public void onSuccess(String url) {
//                System.out.println("url:" + url);
//
//            }
//
//            @Override
//            public void onFail(String error) {
//                System.out.println("error:" + error);
//            }
//        });


        OrmDatabaseHelper orm = new OrmDatabaseHelper(this);
        Dao<User, Integer> dao = orm.getTDao(User.class);

        User user = new User();
        user.setUsername("mao");
        user.setPassword("123456");
        try {
            int count = dao.create(user);
            System.out.println("count:" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
