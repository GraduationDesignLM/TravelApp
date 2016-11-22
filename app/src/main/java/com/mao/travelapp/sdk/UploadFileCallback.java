package com.mao.travelapp.sdk;

/**
 * Created by mao on 2016/11/21.
 */
public interface UploadFileCallback {

    void onSuccess(String url);

    void onFail(String error);
}
