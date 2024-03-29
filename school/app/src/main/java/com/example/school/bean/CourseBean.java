package com.example.school.bean;

public class CourseBean {
    private String courseName;
    private String courseTime;
    private String courstTimeDetail;
    private String courseTeacher;
    private String courseLocation;
    private int id;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getCourstTimeDetail() {
        return courstTimeDetail;
    }

    public void setCourstTimeDetail(String courstTimeDetail) {
        this.courstTimeDetail = courstTimeDetail;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return getCourseName() + "-" + getCourseTime() + "-" + getCourstTimeDetail() + "-" + getCourseTeacher() + "-" + getCourseLocation();
    }

}
