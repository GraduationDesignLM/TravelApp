package com.mao.travelapp.sdk;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mao on 2016/11/11.
 */
public interface QueryCallback<T> extends Serializable{

    void onSuccess(List<T> list);

    void onFail(String error);
}
