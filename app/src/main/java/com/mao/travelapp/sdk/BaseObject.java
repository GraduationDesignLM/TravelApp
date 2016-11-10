package com.mao.travelapp.sdk;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mao on 2016/11/9.
 *
 */
public abstract class BaseObject {

    private final static String URL = HttpManager.BASE_URL + HttpManager.DB_SERVLET_URL;

    final public void save() {

        String json = generateJson();
        System.out.println("json:" + json);
        RequestBody body = new FormBody.Builder()
                .add("json", json)
                .build();

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("body" + body);
            }
        });
    }


    private String generateJson() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\"op\":\"1\",\"table\":\"");
        sb.append(getClass().getSimpleName());
        sb.append("\"");

        try {
            Class<?> clazz = getClass();
            Field[] fields = clazz.getDeclaredFields();
            int length = fields.length;
            for(int i = 0; i < length; i++) {
                //可能为编译自动生成的Field，忽略
                if(fields[i].isSynthetic()) {
                    continue;
                }
                if(i < length - 1) {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(fields[i].getName());
                System.out.println("1111" + fields[i].getName());
                sb.append("\":\"");
                fields[i].setAccessible(true);
                sb.append(fields[i].get(this).toString());
                sb.append("\"");
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
