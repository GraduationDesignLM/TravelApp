package com.mao.imageloader.utils;

import java.text.SimpleDateFormat;

/**
 * Created by mao on 2017/3/27.
 */
public class DateUtils {

    public static String getDate() {

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long time = new Long(System.currentTimeMillis());
        return format.format(time);
    }
}
