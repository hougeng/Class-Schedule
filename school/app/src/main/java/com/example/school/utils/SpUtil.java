package com.example.school.utils;

import android.content.Context;
import android.content.SharedPreferences;

/*
获取字符串
 */
public class SpUtil {
    public static SharedPreferences getSp(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
