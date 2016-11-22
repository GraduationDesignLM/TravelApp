package com.mao.travelapp.sdk;

/**
 * Created by mao on 2016/11/11.
 */
public interface CommonDBCallback {

    void onSuccess(int affectedRowCount);

    void onFail(String error);
}
