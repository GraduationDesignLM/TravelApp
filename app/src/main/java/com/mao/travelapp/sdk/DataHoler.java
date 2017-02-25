package com.mao.travelapp.sdk;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mao on 2017/2/25.
 */
public class DataHoler {

    public static final ConcurrentHashMap<CommonDBCallback, String> commonDBCallbackMap = new ConcurrentHashMap<CommonDBCallback, String>();

    public static final ConcurrentHashMap<QueryCallback, List<?>> queryCallbackMap = new ConcurrentHashMap<QueryCallback, List<?>>();


}
