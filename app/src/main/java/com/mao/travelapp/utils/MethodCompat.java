package com.mao.travelapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by mao on 2017/3/25.
 */
public class MethodCompat {

    public static Drawable getDrawable(Context context, int id) {
        if(context == null) {
            return null;
        }
        return ContextCompat.getDrawable(context, id);
    }
}
