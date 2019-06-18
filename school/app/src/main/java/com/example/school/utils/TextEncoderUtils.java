package com.example.school.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
中文编码gb2312
 */
public class TextEncoderUtils {
    public static String encoding(String text) {
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

}
