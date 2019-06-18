package com.example.school.utils;

import com.example.school.bean.CourseBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ParseCourses {
    private static List<CourseBean> courseList = new ArrayList<>();
    public static List<CourseBean> getKB(String response) {
        response = response.replace("<br>", "hu");
        Document document = Jsoup.parse(response);
        Element table1 = document.getElementById("Table1");//课程表主体的id是table1
        //拿到tbody，http://210.38.137.126:8016/xskbcx.aspx?xh=201611671115&xm=%E6%9D%8E%E4%B8%9A%E8%BE%89&gnmkdm=N121602
        Element tbody = table1.select("tbody").get(0);
        //去除前面两行 ，时间和早晨
        tbody.child(0).remove();
        tbody.child(0).remove();
        //去除上午，下午，晚上顺数下去
        tbody.child(0).child(0).remove();
        tbody.child(4).child(0).remove();
        tbody.child(8).child(0).remove();//第8行第一个
        //去除无用行之后，还剩余
        Elements trs = tbody.select("tr");
        int rowNum = trs.size();
        //用map储存数据
        for (int i = 0; i < rowNum; i++) {
            if (i % 2 == 0) {
                //拿到每一行
                Element tr = trs.get(i);
                //一共有多少列 -2 晚自习有三节课，手动去掉最后一行，即第十一节课
                int columnNum = tr.childNodeSize() - 2; //子节点数量，即减少开始两列
                for (int j = 1; j < columnNum - 1; j++) {
                    String timeDetail = null;
                    switch (i) {
                        case 0:
                            timeDetail = "8:10-9:50";
                            break;
                        case 2:
                            timeDetail = "10:15-11:55";
                            break;
                        case 4:
                            timeDetail = "14:30-16:10";
                            break;
                        case 6:
                            timeDetail = "16:25-18:05";
                            break;
                        case 8:
                            timeDetail = "19:30-21:10";
                    }

                    Element colum = tr.child(j);
                    if (colum.hasAttr("rowspan")) {
                        CourseBean course = new CourseBean();
                        String text = colum.text();
                        //信息资源组织与管理 周一第1,2节{第1-8周} 周香庭 主楼613(多)
                        String[] strings = text.split("hu");
                        String name = "";
                        try {
                            if (strings.length > 4) {
                                name = strings[0] + "-" + strings[1].substring(strings[1].indexOf("|") + 1, strings[1].indexOf("}"));
                                String name2 = strings[5] + "-" + strings[6].substring(strings[6].indexOf("|") + 1, strings[6].indexOf("}"));
                                name = name + "\n" + name2;
                            } else {
                                name = strings[0];
                                if (strings[1].contains("单周")) {
                                    name = name + " -单周";
                                } else if (strings[1].contains("双周")) {
                                    name = name + " -双周";
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            course.setCourseName("暂无");
                        }
                        course.setCourseName(name);
                        course.setCourseTime(String.valueOf(j));
                        course.setCourstTimeDetail(timeDetail);
                        /**
                         * 有的课程不规范，缺少信息，导致解析错误，程序崩溃
                         */
                        String teacher = "";
                        try {
                            teacher = strings[2];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            teacher = "暂无";
                        }
                        String location = "";
                        try {
                            location = strings[3];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            location = "暂无";
                        }
                        course.setCourseTeacher(teacher);
                        course.setCourseLocation(location);
                        courseList.add(course);
                    }
                }
            }
        }
        return courseList;
    }

}
