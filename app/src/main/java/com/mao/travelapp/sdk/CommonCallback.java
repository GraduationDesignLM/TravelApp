package com.mao.travelapp.sdk;

/**
 * Created by mao on 2016/11/11.
 */
public interface CommonCallback {

    void onSuccess(int affectedRowCount);

    void onFail(String error);
}
