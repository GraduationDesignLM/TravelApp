package com.mao.travelapp.sdk;

import java.io.Serializable;

/**
 * Created by mao on 2016/11/21.
 */
public interface UploadFileCallback extends Serializable{

    void onSuccess(String url);

    void onFail(String error);
}
