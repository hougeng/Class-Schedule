package com.example.school.event;

public class UpdateDataEvent {
    private String msg;

    public UpdateDataEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
