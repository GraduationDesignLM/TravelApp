package com.mao.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mao.travelapp.sdk.UploadFileCallback;
import com.mao.travelapp.sdk.FileHelper;

public class MainActivity extends AppCompatActivity {

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




        //文件测试
        String path = "/sdcard/123.jpg";
        FileHelper.uploadOne(path, new UploadFileCallback() {

            @Override
            public void onSuccess(String url) {
                System.out.println("url:" + url);
            }

            @Override
            public void onFail(String error) {
                System.out.println("error:" + error);
            }
        });

    }

}
