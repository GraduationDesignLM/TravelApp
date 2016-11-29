package com.mao.travelapp.sdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mao on 2016/11/21.
 */
public class FileHelper {

    private final static String URL = HttpManager.BASE_URL + HttpManager.UPLOADFILE_SERVLET_URL;

    private final static Handler sHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                //上传一个文件回调消息处理
                case 0:
                    UploadFileCallback callback = (UploadFileCallback) bundle.get("callback");
                    String text = (String) bundle.get("text");
                    int arg = msg.arg1;
                    if(arg == 0) {
                        callback.onSuccess(text);
                    } else if(arg == 1) {
                        callback.onFail(text);
                    }
                    break;

                default:
            }

        }
    };


    /**
     * 上传一个文件
     *
     * @param path 文件所在路径
     * @param callback 回调接口
     */
    public final static void uploadOne(String path, final UploadFileCallback callback) {

        if(TextUtils.isEmpty(path)) {

        }
        File file = new File(path);

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + "123" + "\""), fileBody)
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
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.arg1 = 1;//失败
                    Bundle data = new Bundle();
                    data.putSerializable("callback", callback);
                    data.putString("text", e.getMessage());
                    msg.setData(data);
                    sHandler.sendMessage(msg);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(callback != null) {
                    String result = response.body().string();
                    Message msg = Message.obtain();
                    msg.what = 0;
                    String text;
                    //不是0表示上传成功，得到文件路径
                    if(!TextUtils.isEmpty(result) && !"0".equals(result.trim())) {
                        text = result.trim();
                        msg.arg1 = 0;//成功
                    } else {
                        text = "上传失败";
                        msg.arg1 = 1;//失败
                    }
                    Bundle data = new Bundle();
                    data.putSerializable("callback", callback);
                    data.putString("text", text);
                    msg.setData(data);
                    sHandler.sendMessage(msg);
                }
            }
        });

    }
}



