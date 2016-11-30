package com.mao.travelapp.sdk;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mao on 2016/11/21.
 */
public interface UploadFileCallback extends Serializable{

    void onSuccess(List<String> urls);

    void onFail(String error);
}
