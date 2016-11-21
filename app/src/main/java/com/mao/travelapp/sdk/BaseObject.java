package com.mao.travelapp.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    /**
     * 添加一条记录
     */
    final public void save() {
        save(null);
    }

    /**
     * 添加一条记录
     *
     * @param callback 回调接口
     */
    final public void save(final CommonCallback callback) {
        String json = generateAddOrUpdateJson(1);
        addOrUpdateOrDelete(json, callback);
    }

    /**
     * 更新指定id的数据
     */
    final public void update() {
        update(null);
    }

    /**
     * 更新指定id的数据
     *
     * @param callback 回调接口
     */
    final public void update(final CommonCallback callback) {
        String json = generateAddOrUpdateJson(3);
        addOrUpdateOrDelete(json, callback);
    }

    /**
     * 删除一条记录
     *
     * @param where 条件， 至少必须包含一个条件
     */
    final public static void delete(Map<String, String> where, Class<?> clazz) {
        delete(where, clazz, null);
    }

    /**
     * 删除一条记录
     *
     * @param where 条件， 至少必须包含一个条件
     * @param callback 回调接口
     */
    final public static void delete(Map<String, String> where, Class<?> clazz, final CommonCallback callback) {
        String json = generateDeleteOrQueryJson(2, where, clazz);
        addOrUpdateOrDelete(json, callback);
    }

    /**
     * 查询
     *
     * @param where 条件，size为0表示查询所有记录
     * @param clazz 要查询的表对应的实体类的Class对象
     * @param callback 回调
     * @param <T> 要查询的表对应的实体类类型
     */
    final public static <T> void query(Map<String, String> where, Class<T> clazz, QueryCallback<T> callback) {
        String json = generateDeleteOrQueryJson(4, where, clazz);
        query(json, clazz, callback);
    }

    private String generateAddOrUpdateJson(int op) {
        StringBuilder sb = generateCommonsonHeader(op, getClass());
        try {
            Class<?> clazz = getClass();
            Field[] fields = clazz.getDeclaredFields();
            int length = fields.length;
            if(length > 0) {
                sb.append(",");
            }
            for(int i = 0; i < length; i++) {
                //可能为编译自动生成的Field，忽略
                if(fields[i].isSynthetic()) {
                    continue;
                }
                if(i > 0) {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(fields[i].getName());
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

    private static String generateDeleteOrQueryJson(int op, Map<String, String> where, Class<?> clazz) {
        StringBuilder sb = generateCommonsonHeader(op, clazz);
        Set<Map.Entry<String, String>> entrys = where.entrySet();
        int i = 0;
        int size = entrys.size();
        for(Map.Entry<String, String> e : entrys) {
            if(i < size) {
                sb.append(",");
            }
            sb.append("\"");
            sb.append(e.getKey());
            sb.append("\":\"");
            sb.append(e.getValue());
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 生成请求json通用头部
     * 最后生成的字符串如下：
     * {
     *     "op":"",
     *     "table":""
     *
     *
     * @param op
     * @return
     */
    private static StringBuilder generateCommonsonHeader(int op, Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"op\":" + op + ",\"table\":\"");
        sb.append(clazz.getSimpleName());
        sb.append("\"");
        return sb;
    }

    private static void addOrUpdateOrDelete(String json, final CommonCallback callback) {
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
                if(callback != null) {
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(callback != null) {
                    callback.onSuccess(Integer.valueOf(response.body().string()));
                }
            }
        });
    }

    private static <T> void query(String json, final Class<T> clazz, final QueryCallback<T> callback) {

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
                if(callback != null) {
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(callback != null) {
                    String json = response.body().string();

                    List<T> list = new ArrayList<T>();
                    JsonParser parser = new JsonParser();
                    JsonArray arr = parser.parse(json).getAsJsonArray();

                    Gson gson = new Gson();
                    for(JsonElement e : arr) {
                        list.add(gson.fromJson(e, clazz));
                    }
                    callback.onSuccess(list);

                }
            }
        });
    }

}
