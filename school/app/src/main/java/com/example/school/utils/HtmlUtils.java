package com.example.school.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
/*
对中文编码
 */
public class HtmlUtils {
    private String response;

    public HtmlUtils(String response) {
        this.response = response;
    }

    /*
    获取学生姓名
     */
    public String getXhandName() {
        Document document = Jsoup.parse(response);
        Element xhxm = document.getElementById("xhxm");//返回对拥有指定 ID 的第一个对象的引用。
        String text = xhxm.text();
        return text;
    }





}
