package com.mao.travelapp.sdk;

import java.io.Serializable;

/**
 * Created by mao on 2016/11/11.
 */
public interface CommonDBCallback extends Serializable{

    void onSuccess(int affectedRowCount);

    void onFail(String error);
}
